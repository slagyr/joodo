(ns joodo.middleware.refresh
  (:use
    [fresh.core :only (freshener ns-to-file)]))

(defn- files-to-keep-fresh []
  (filter identity (map #(ns-to-file (.name %)) (all-ns))))

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

(defn wrap-refresh [handler]
  (let [refresh! (freshener files-to-keep-fresh report-refresh)]
    (fn [request]
      (refresh!)
      (clear-controller-caches)
      (handler request))))
