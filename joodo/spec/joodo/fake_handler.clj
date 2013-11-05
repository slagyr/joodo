(ns joodo.fake-handler)

(defn handler [request]
  {:body "handler"})

(defn other-handler [request]
  {:body "other handler"})