(ns joodo.kake.servlet-spec
  (:use
    [speclj.core]
    [joodo.kake.servlet]
    [joodo.middleware.verbose :only (wrap-verbose)]
    [joodo.middleware.refresh :only (wrap-refresh)]
    [joodo.env :only (*env* env)]
    [joodo.middleware.servlet-session :only (wrap-servlet-session)])
  (:import
    [filecabinet FakeFileSystem]))

(deftype FakeServlet []
  joodo.kake.servlet.HandlerInstallable
  (install-handler [this handler] handler))

(defn fake-wrapper [key]
  (fn [handler]
    (fn [request]
      (assoc (handler request) key true))))

(describe "Servlet methods"

  (with fs (FakeFileSystem/installed))
  (before (swap! *env* assoc :joodo.core.namespace "joodo.kake.test-core"))
  (around [it]
    (binding [wrap-verbose (fake-wrapper :verbose)
              wrap-refresh (fake-wrapper :refresh)
              wrap-servlet-session (fake-wrapper :servlet-session)]
      (it)))
  (after (System/setProperty "joodo.ignore.config" "false"))

  (it "extracts the app handler"
    (binding [build-joodo-handler (fake-wrapper :fake-joodo-handler)]
      (let [handler (extract-joodo-handler)
            response (handler {})]
        (should= true (:app-handler response))
        (should= true (:fake-joodo-handler response)))))

  (it "extracts joodo-handler if it is defined"
    (swap! *env* assoc :joodo.core.namespace "joodo.kake.test-override-core")
    (let [handler (extract-joodo-handler)
          response (handler {})]
      (should-not (:app-handler response))
      (should= true (:joodo-handler response))))

  (it "initializes a servlet in non-development mode"
    (swap! *env* assoc :joodo-env "blah")
    (binding [load-configurations (fn [])]
      (let [handler (initialize-joodo-servlet (FakeServlet.))
            response (handler {})]
        (should= true (:app-handler response))
        (should= nil (:development response)))))

  (it "initializes a servlet in development mode"
    (swap! *env* assoc :joodo-env "development")
    (binding [load-configurations (fn [])]
      (let [handler (initialize-joodo-servlet (FakeServlet.))
            response (handler {})]
        (should= true (:app-handler response))
        (should= true (:verbose response))
        (should= true (:refresh response)))))

  (it "loads a config file"
    (.createTextFile @fs "config/environment.clj" "(use 'joodo.env)(swap! *env* assoc :root-conf *ns*)")
    (.createTextFile @fs "config/test.clj" "(use 'joodo.env)(swap! *env* assoc :env-conf *ns*)")
    (System/setProperty "joodo.env" "test")
    (load-configurations)
    (should-not= nil (env :root-conf))
    (should-not= nil (env :env-conf))
    (should= true (.startsWith (name (.getName (env :env-conf))) "joodo.config-"))
    (should= (env :root-conf) (env :env-conf)))

  (it "loads a config file from env dir"
    (.createTextFile @fs "config/environment.clj" "(use 'joodo.env)(swap! *env* assoc :root-conf *ns*)")
    (.createTextFile @fs "config/test.clj" "(use 'joodo.env)(swap! *env* assoc :env-conf *ns*)")
    (System/setProperty "joodo.env" "test")
    (System/setProperty "joodo.ignore.config" "true")
    (reset! *env* {:joodo.core.namespace "joodo.kake.test-override-core"})
    (initialize-joodo-servlet (FakeServlet.))
    (should= nil (env :root-conf))
    (should= nil (env :env-conf)))


  )

(run-specs)
