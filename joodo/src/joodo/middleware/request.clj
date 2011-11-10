(ns joodo.middleware.request)

(def *request* {})

(defn wrap-bind-request
  "Makes the *request* var available and sets
  its value to contain information about the
  current HTTP request"
  [handler]
  (fn [request]
    (binding [*request* request]
      (handler request))))
