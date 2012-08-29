(ns joodo.kake.servlet-spec
  (:use [speclj.core]
        [joodo.kake.servlet]
        [joodo.middleware.verbose :only (wrap-verbose)]
        [joodo.middleware.refresh :only (wrap-refresh)]
        [joodo.env :only (alter-env! set-env! env load-configurations)]
        [joodo.middleware.servlet-session :only (wrap-servlet-session)])
  (:import [filecabinet FakeFileSystem]))

(deftype FakeServlet []
  joodo.kake.servlet.HandlerInstallable
  (install-handler [this handler] handler))

(defn fake-wrapper [key]
  (fn [handler]
    (fn [request]
      (assoc (handler request) key true))))

(describe "Servlet methods"

  (with fs (FakeFileSystem/installed))
  (before (alter-env! assoc :joodo.root.namespace "joodo.kake.test-core"))
  (around [it]
    (with-redefs [wrap-verbose (fake-wrapper :verbose )
                  wrap-refresh (fake-wrapper :refresh )
                  wrap-servlet-session (fake-wrapper :servlet-session )]
      (it)))
  (after (System/setProperty "joodo.ignore.config" "false"))

  (it "extracts the app handler"
    (with-redefs [build-joodo-handler (fake-wrapper :fake-joodo-handler )]
      (let [handler (extract-joodo-handler)
            response (handler {})]
        (should= true (:app-handler response))
        (should= true (:fake-joodo-handler response)))))

  (it "extracts joodo-handler if it is defined"
    (alter-env! assoc :joodo.root.namespace "joodo.kake.test-override-core")
    (let [handler (extract-joodo-handler)
          response (handler {})]
      (should-not (:app-handler response))
      (should= true (:joodo-handler response))))

  (it "initializes a servlet in non-development mode"
    (alter-env! assoc :joodo-env "blah")
    (with-redefs [load-configurations (fn [])]
      (let [handler (initialize-joodo-servlet (FakeServlet.))
            response (handler {})]
        (should= true (:app-handler response))
        (should= nil (:development response)))))

  (it "initializes a servlet in development mode"
    (alter-env! assoc :joodo-env "development")
    (with-redefs [load-configurations (fn [])]
      (let [handler (initialize-joodo-servlet (FakeServlet.))
            response (handler {})]
        (should= true (:app-handler response))
        (should= true (:verbose response))
        (should= true (:refresh response)))))
)

(run-specs)
