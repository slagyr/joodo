(ns sample.core
  (:use
    [compojure.core :only (defroutes GET)]
    [compojure.route :only (not-found)]
    [joodo.middleware.view-context :only (wrap-view-context)]
    [joodo.views :only (render-template render-html)]
    [joodo.controllers :only (controller-router)]))

(defroutes sample-routes
  (GET "/" [] (render-template "index"))
  (controller-router 'sample.controller)
  (not-found (render-template "not_found" :template-root "sample/view" :ns `sample.view.view-helpers)))

(def app-handler
  (->
    sample-routes
    (wrap-view-context :template-root "sample/view" :ns `sample.view.view-helpers)))

