(ns joodo.controllers-spec
  (:use
    [speclj.core]
    [joodo.controllers]))

(describe "Controllers"

  (before (clear-controller-caches))

  (it "determines the namespaces for a request"
    (should= [] (namespaces-for-path "root" "/"))
    (should= ["root.one-controller" "root.one.one-controller"] (namespaces-for-path "root" "/one"))
    (should= ["root.one-controller" "root.one.one-controller"] (namespaces-for-path "root" "/one/"))
    (should= ["root.one-controller" "root.one.one-controller"
              "root.one.two-controller" "root.one.two.two-controller"] (namespaces-for-path "root" "/one/two"))
    (should= ["root.one-controller" "root.one.one-controller"
              "root.one.two-controller" "root.one.two.two-controller"
              "root.one.two.three-controller" "root.one.two.three.three-controller"] (namespaces-for-path "root" "/one/two/three"))
    (should= ["root.one-controller" "root.one.one-controller"
              "root.one.two-controller" "root.one.two.two-controller"
              "root.one.two.blah-controller" "root.one.two.blah.blah-controller"] (namespaces-for-path "root" "/one/two.blah")))

  (context "with ns loading"
    (with required-nses (atom []))
    (with resolved-var (atom nil))
    (with ns-to-find (atom :fake-ns))
    (with controller-to-resolve (atom (fn [request] (str "RESPONSE: " (:uri request)))))
    (around [spec]
      (binding [require (fn [name] (swap! @required-nses conj name))
                find-ns (fn [name] @@ns-to-find)
                ns-resolve (fn [ns var] (reset! @resolved-var var) @@controller-to-resolve)]
        (spec)))

    (it "controller-router creates a ring-handler that will dynamically load controllers"
      (let [router (controller-router 'root)
            response (router {:uri "/one"})]
        (should= "RESPONSE: /one" response)
        (should= ['root.one-controller] @@required-nses)
        (should= 'one-controller @@resolved-var)))

    (it "controller-router reuses previously loaded controllers"
      (let [router (controller-router 'root)]
        (should= "RESPONSE: /one" (router {:uri "/one"}))
        (should= ['root.one-controller] @@required-nses)
        (reset! @required-nses [])
        (should= "RESPONSE: /one" (router {:uri "/one"}))
        (should= [] @@required-nses)))

    (it "controller-router can handle nested namespaces"
      (let [router (controller-router 'root)]
        (reset! @controller-to-resolve (fn [request] (if (= "/one" (:uri request)) "RESPONSE: /one")))
        (let [response (router {:uri "/one"})]
          (should= "RESPONSE: /one" response)
          (should= ['root.one-controller] @@required-nses)
          (should= 'one-controller @@resolved-var))
        (reset! @required-nses [])
        (reset! @controller-to-resolve (fn [request] nil))
        (reset! @ns-to-find :nested-fake-ns)
        (let [response (router {:uri "/one/two"})]
          (should= nil response)
          (should= ['root.one.one-controller 'root.one.two-controller] @@required-nses)
          (should= 'two-controller @@resolved-var))))

    (it "resolve-controller can handle unhandled routes"
      (reset! @ns-to-find nil)
      (let [router (controller-router 'root)]
        (should= nil (router {:uri "/one"})))))

(it "handles nonexisting namespaces"
  (binding [require (fn [name] (throw (java.io.FileNotFoundException. "Doesn't exist.")))]
    (should= nil (resolve-controller 'the.missing-controller)))))

(run-specs)