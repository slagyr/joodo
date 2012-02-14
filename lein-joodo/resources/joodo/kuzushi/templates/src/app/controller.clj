(ns !-CONTROLLER_NS-!
  (:use
    [compojure.core]
    [ring.util.response :only (redirect)]
    [joodo.views :only (render-template render-html)]))

(defroutes !-CONTROLLER_NAME-!
  (GET "/!-CONTROLLER_SUBJECT-!" [] (redirect "!-CONTROLLER_SUBJECT-!/index"))
  (context "/!-CONTROLLER_SUBJECT-!" []
    (GET "/test" [] {:status 200 :body "PASS"})))