(ns ^{:doc "This namespace contains functions that interface with the fresh library. The fresh library automatically loads up files that have recently been changed."}
  joodo.middleware.refresh
  (:use
    [fresh.core :only (freshener ns-to-file)]))

(defn- files-to-keep-fresh []
  (let [ns-es (all-ns)
        ns-es (filter #(not (.startsWith (.toString (.name %)) "joodo")) ns-es)]
    (filter identity (map #(ns-to-file (.name %)) ns-es))))

(defn- report-refresh [report]
  (when-let [reloaded (seq (:reloaded report))]
    (println "Reloading...")
    (doseq [file reloaded] (println file))
    (println ""))
  true)

(defn- clear-controller-caches []
  (try
    ; To avoid dependency on kake, we dynamically invoke the clear-controller-caches fn.
    (require 'joodo.controllers)
    (let [controllers-ns (find-ns 'joodo.controllers)
          clear-fn (ns-resolve controllers-ns 'clear-controller-caches)]
      (clear-fn))
    (catch Exception e
      (println "ON NO!!!  Can't clear controller cache." e))))

(defn wrap-refresh
  "When integrated with the current ring handler, this function reloads newly saved files (such as controllers) while in development mode."
  [handler]
  (let [refresh! (freshener files-to-keep-fresh report-refresh)]
    (fn [request]
      (refresh!)
      (clear-controller-caches)
      (handler request))))
