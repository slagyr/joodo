(ns ^{:doc "This namespace contains logic that stores information about the current HTTP request."}
  joodo.middleware.request)

(def ^{:doc "Holds information about the
  current request in map form."}
  *request* {})

(defn wrap-bind-request
  "Makes the *request* var available and sets
  its value to contain information about the
  current HTTP request"
  [handler]
  (fn [request]
    (binding [*request* request]
      (handler request))))
