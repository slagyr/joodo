(ns joodo.handler-spec
  (:use [speclj.core]
        [joodo.handler]
        [joodo.middleware.verbose :only (wrap-verbose)]
        [joodo.middleware.refresh :only (wrap-refresh)]
        [joodo.env :only (alter-env! set-env! env load-configurations)])
  (:import [filecabinet FakeFileSystem]))

(defn fake-wrapper [key]
  (fn [handler]
    (fn [request]
      (assoc (handler request) key true))))

(describe "Servlet methods"

  (with fs (FakeFileSystem/installed))
  (before (alter-env! assoc :joodo.root.namespace "joodo.test-core"))
  (around [it]
    (with-redefs [wrap-verbose (fake-wrapper :verbose )
                  wrap-refresh (fake-wrapper :refresh )]
      (binding [*public-dir* "sample"]
        (it))))
  (after (System/setProperty "joodo.ignore.config" "false"))

  (it "extracts the app handler"
    (with-redefs [build-joodo-handler (fake-wrapper :fake-joodo-handler )]
      (let [handler (extract-joodo-handler)
            response (handler {})]
        (should= true (:app-handler response))
        (should= true (:fake-joodo-handler response)))))

  (it "extracts joodo-handler if it is defined"
    (alter-env! assoc :joodo.root.namespace "joodo.test-override-core")
    (let [handler (extract-joodo-handler)
          response (handler {})]
      (should-not (:app-handler response))
      (should= true (:joodo-handler response))))
)

(run-specs)
