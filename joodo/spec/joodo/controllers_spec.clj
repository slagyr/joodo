(ns joodo.controllers-spec
  (:use
    [speclj.core]
    [joodo.controllers]))

(describe "Controllers"

  (before (clear-controller-caches))

  (it "determines the namespaces for a request"
    (should= [] (namespaces-for-path "root" "/"))
    (should= ["root.one-controller"] (namespaces-for-path "root" "/one"))
    (should= ["root.one-controller"] (namespaces-for-path "root" "/one/"))
    (should= ["root.one-controller" "root.one.two-controller"] (namespaces-for-path "root" "/one/two"))
    (should= ["root.one-controller" "root.one.two-controller" "root.one.two.three-controller"] (namespaces-for-path "root" "/one/two/three"))
    (should= ["root.one-controller" "root.one.two-controller" "root.one.two.blah-controller"] (namespaces-for-path "root" "/one/two.blah")))

  (it "controller-router creates a ring-handler will dynamically load controllers"
    (let [required-ns (atom nil)
          resolved-var (atom nil)
          router (controller-router 'root)]
      (binding [require (fn [name] (reset! required-ns name))
                find-ns (fn [name] :fake-ns)
                ns-resolve (fn [ns var] (reset! resolved-var var) (fn [request] :fake-response))]
        (let [response (router {:uri "/one"})]
          (should= :fake-response response)
          (should= 'root.one-controller @required-ns)
          (should= 'one-controller @resolved-var)))))

  (it "controller-router reuses previously loaded controllers"
    (let [required-ns (atom nil)
          resolved-var (atom nil)
          router (controller-router 'root)]
      (binding [require (fn [name] (reset! required-ns name))
                find-ns (fn [name] :fake-ns)
                ns-resolve (fn [ns var] (reset! resolved-var var) (fn [request] :fake-response))]
        (should= :fake-response (router {:uri "/one"})))
      (should= :fake-response (router {:uri "/one"}))))

  (it "controller-router can handle nested namespaces"
    (let [required-ns (atom nil)
          resolved-var (atom nil)
          router (controller-router 'root)]
      (binding [require (fn [name] (reset! required-ns name))
                find-ns (fn [name] :fake-ns)
                ns-resolve (fn [ns var] (reset! resolved-var var) (fn [request] (if (= "/one" (:uri request)) :fake-response)))]
        (let [response (router {:uri "/one"})]
          (should= :fake-response response)
          (should= 'root.one-controller @required-ns)
          (should= 'one-controller @resolved-var)))
      (binding [require (fn [name] (reset! required-ns name))
                find-ns (fn [name] :nested-fake-ns)
                ns-resolve (fn [ns var] (reset! resolved-var var) (fn [request] :nested-fake-response))]
        (let [response (router {:uri "/one/two"})]
          (should= :nested-fake-response response)
          (should= 'root.one.two-controller @required-ns)
          (should= 'two-controller @resolved-var)))))

  (it "resolve-controller can handle unhandled routes"
    (let [required-ns (atom nil)
          resolved-var (atom nil)
          router (controller-router 'root)]
      (binding [require (fn [name] (reset! required-ns name))
                find-ns (fn [name] nil)
                ns-resolve (fn [ns var] (reset! resolved-var var) (fn [request] :fake-response))]
        (should= nil (router {:uri "/one"})))))

  (it "handles nonexisting namespaces"
    (binding [require (fn [name] (throw (java.io.FileNotFoundException. "Doesn't exist.")))]
      (should= nil (resolve-controller 'the.missing-controller))))

    )

(run-specs)