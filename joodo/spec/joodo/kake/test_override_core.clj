(ns joodo.kake.test-override-core)

(defn joodo-handler [request]
  (assoc request :joodo-handler true))
