(ns {{name}}.main
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [joodo.env :as env]
            [joodo.middleware.favicon :refer [wrap-favicon-bouncer]]
            [joodo.middleware.keyword-cookies :refer [wrap-keyword-cookies]]
            [joodo.middleware.request :refer [wrap-bind-request]]
            [joodo.middleware.util :refer [wrap-development-maybe]]
            [joodo.middleware.view-context :refer [wrap-view-context]]
            [joodo.views :refer [render-template render-html]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [ring.middleware.flash :refer [wrap-flash]]
            [ring.middleware.head :refer [wrap-head]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.session :refer [wrap-session]]
            [shoreleave.middleware.rpc :refer [wrap-rpc]]
            ))

(env/load-configurations)

(defroutes app-routes
  (GET "/" [] (render-template "index"))
  (route/resources "/") ; this is rather redundant with the (wrap-file "public") below.
  (route/not-found (render-template "not_found" :template-root "{{nested-dirs}}" :ns `{{name}}.view-helpers)))

(def app-handler
  (->
    app-routes
    (wrap-view-context :template-root "{{nested-dirs}}" :ns `{{name}}.view-helpers)
    wrap-rpc))

(def app
  (-> app-handler
    wrap-development-maybe
    wrap-bind-request
    wrap-keyword-params
    wrap-params
    wrap-multipart-params
    wrap-flash
    wrap-keyword-cookies
    wrap-session
    wrap-favicon-bouncer
    (wrap-file "public") ; this is rather redundant with the route/resources above.
    wrap-file-info
    wrap-head))