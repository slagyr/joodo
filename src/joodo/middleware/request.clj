(ns joodo.middleware.request)

(def *request* {})

(defn wrap-bind-request [handler]
  (fn [request]
    (binding [*request* request]
      (handler request))))
