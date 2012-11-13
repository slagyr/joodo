(ns joodo.main
  (:require [joodo.handler :refer [load-handler]]
            [joodo.env :refer [env load-configurations]]
            [ring.adapter.jetty :refer [run-jetty]]
            [chee.util :refer [->options]]))

(def default-options {:port 8080
                      :host "127.0.0.1"
                      :environment "development"})

(defn- keywordize-keys [options]
  (into {} (map (fn [[k v]] [(keyword k) v]) options)))

(defn- sanitize-options [options]
  (if (:port options)
    (assoc options :port (Integer/parseInt (:port options)))
    options))

(defn -main [& args]
  (let [options (->options args)
        options (keywordize-keys options)
        options (sanitize-options options)
        options (merge default-options options)]
    (System/setProperty "joodo.env" (:environment options))
    (load-configurations)
    (println "Loaded environment: " (env :joodo-env))
    (let [handler (load-handler)]
      (run-jetty handler options))))
