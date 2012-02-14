(ns !-APP_NAME-!.core
  (:use
    [compojure.core :only (defroutes GET)]
    [compojure.route :only (not-found)]
    [joodo.middleware.view-context :only (wrap-view-context)]
    [joodo.views :only (render-template render-html)]
    [joodo.controllers :only (controller-router)]))

(defroutes !-APP_NAME-!-routes
  (GET "/" [] (render-template "index"))
  (controller-router '!-APP_NAME-!.controller)
  (not-found (render-template "not_found" :template-root "!-DIR_NAME-!/view" :ns `!-APP_NAME-!.view.view-helpers)))

(def app-handler
  (->
    !-APP_NAME-!-routes
    (wrap-view-context :template-root "!-DIR_NAME-!/view" :ns `!-APP_NAME-!.view.view-helpers)))

