(ns joodo.middleware.request-spec
  (:use
    [speclj.core]
    [joodo.middleware.request]))

(describe "Request Middleware"

  (with handled-request (atom nil))
  (with mock-handler
    (fn [request]
      (reset! @handled-request *request*)
      {}))
  (with wrapper (wrap-bind-request @mock-handler))

  (it "binds *request*"
    (@wrapper {:uri "/foo"})
    (should= {:uri "/foo"} @@handled-request)

    (@wrapper {:uri "/bar" :params {:a 1}})
    (should= {:uri "/bar" :params {:a 1}} @@handled-request))

  )

(run-specs)

