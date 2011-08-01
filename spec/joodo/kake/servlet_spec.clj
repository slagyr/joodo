(ns joodo.kake.servlet-spec
  (:use
    [speclj.core]
    [joodo.kake.servlet]
    [joodo.middleware.verbose :only (wrap-verbose)]
    [joodo.middleware.refresh :only (wrap-refresh)]
    [joodo.env :only (env)]
    [joodo.middleware.servlet-session :only (wrap-servlet-session)]))
(deftype FakeServlet []
  joodo.kake.servlet.HandlerInstallable
  (install-handler [this handler] handler))

(defn fake-wrapper [key]
  (fn [handler]
    (fn [request]
      (assoc (handler request) key true))))

(describe "Servlet methods"

  (before (System/setProperty "joodo.core.namespace" "joodo.kake.test-core"))
  (around [it]
    (binding [wrap-verbose (fake-wrapper :verbose)
              wrap-refresh (fake-wrapper :refresh)
              wrap-servlet-session (fake-wrapper :servlet-session)]
      (it)))

  (it "extracts the app handler"
    (binding [build-joodo-handler (fake-wrapper :fake-joodo-handler)]
      (let [handler (extract-joodo-handler)
            response (handler {})]
        (should= true (:app-handler response))
        (should= true (:fake-joodo-handler response)))))

  (it "extracts joodo-handler if it is defined"
    (System/setProperty "joodo.core.namespace" "joodo.kake.test-override-core")
    (let [handler (extract-joodo-handler)
          response (handler {})]
      (should-not (:app-handler response))
      (should= true (:joodo-handler response))))

  (it "initializes a servlet in non-development mode"
    (alter-var-root #'env (fn [_] "blah"))
    (let [handler (initialize-joodo-servlet (FakeServlet.))
          response (handler {})]
      (should= true (:app-handler response))
      (should= nil (:development response))))

  (it "initializes a servlet in development mode"
    (alter-var-root #'env (fn [_] "development"))
    (let [handler (initialize-joodo-servlet (FakeServlet.))
          response (handler {})]
      (should= true (:app-handler response))
      (should= true (:verbose response))
      (should= true (:refresh response))))

  )

(run-specs)
