(ns joodo.middleware.util)

(defn- attempt-to-load-var [ns-sym var-sym]
  (try
    (require ns-sym)
    (let [ns (the-ns ns-sym)]
      (ns-resolve ns var-sym))
    (catch Exception e
      (println "Failed to load var:" var-sym "from ns:" ns-sym e)
      nil)))

(defn attempt-wrap [handler ns-sym var-sym]
  (if-let [wrapper (attempt-to-load-var ns-sym var-sym)]
    (wrapper handler)
    (do
      (println "Bypassing" var-sym)
      handler)))