(ns joodo.test-core)

(defn app-handler [request]
  (assoc request :app-handler true))
