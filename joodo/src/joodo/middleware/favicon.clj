(ns ^{:doc "This namespace is for favicon.ico requests."}
  joodo.middleware.favicon)

(defn- is-favicon-request? [request]
  (= [:get "/favicon.ico"] [(:request-method request) (:uri request)]))

(defn wrap-favicon-bouncer
  "Responds with 404 for /favicon.ico requests."
  [handler]
  (fn [request]
    (if (is-favicon-request? request)
      {:status 404 :headers {} :body ""}
      (handler request))))

