(ns joodo.middleware.favicon-spec
  (:use
    [speclj.core]
    [joodo.middleware.favicon]))

(describe "favicon-ignoring middleware"
  (with updated-request (atom nil))
  (with mock-handler
    (fn [request]
      (reset! @updated-request request)
      {:status 200 :body "more processing"}))

  (it "ignores favicon GET requests"
    (let [request-data {:uri "/favicon.ico"
                        :request-method :get}
          handler (wrap-favicon-bouncer @mock-handler)]
      (should= {:status 404 :headers {} :body ""}
               (handler request-data))))

  (it "processes favicon POST requests (in case that happens)"
    (let [request-data {:uri "/favicon.ico"
                        :request-method :post}
          handler (wrap-favicon-bouncer @mock-handler)]
      (should= {:status 200 :body "more processing"}
               (handler request-data))))

  (it "processes non-favicon requests"
    (let [request-data {:uri "/someplace"
                        :request-method :get}
          handler (wrap-favicon-bouncer @mock-handler)]
      (should= {:status 200 :body "more processing"}
               (handler request-data)))))

