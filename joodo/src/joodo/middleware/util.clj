(ns joodo.middleware.util)

(defn- attempt-to-load-var [qualified-sym]
  (let [ns-sym (symbol (namespace qualified-sym))
        var-sym (symbol (name qualified-sym))]
    (try
      (require ns-sym)
      (let [ns (the-ns ns-sym)]
        (ns-resolve ns var-sym))
      (catch Exception e
        (println "Failed to load var:" var-sym "from ns:" ns-sym e)
        nil))))

(defn attempt-wrap
  "Attempts to wrap the handler with the specified middlware.
  The middleware-sym must be a namespaced symbol (myproject.some-namespace/wrapper-var).
  The namespace will be loaded, the wrapper var will be resolved and applied to the handler.
  If the var can not be loaded or resolved, an error will be logged and the unwrapped handler returned."
  [handler middleware-sym]
  (if-let [wrapper (attempt-to-load-var middleware-sym)]
    (wrapper handler)
    (do
      (println "Bypassing" middleware-sym)
      handler)))