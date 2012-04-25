(ns joodo.middleware.view-context-spec
  (:use
    [speclj.core]
    [joodo.middleware.view-context]
    [joodo.views :only (*view-context*)]))

(describe "View Context Middleware"

  (with view-context (atom nil))
  (with mock-handler
    (fn [request]
      (reset! @view-context *view-context*)
      {}))
  (with wrapper (wrap-view-context @mock-handler))

  (it "defaults to default view-context"
    ((wrap-view-context @mock-handler) {})
    (should= *view-context* @@view-context))

  (it "sets the view-context during a request"
    ((wrap-view-context @mock-handler :ns `test-ns :template-root "test-root") {})
    (should= `test-ns (:ns @@view-context))
    (should= "test-root" (:template-root @@view-context)))

  (it "can also take a map as the first param"
    ((wrap-view-context @mock-handler {:foo "foo"} :bar "bar") {})
    (should= "foo" (:foo @@view-context))
    (should= "bar" (:bar @@view-context))))

(run-specs)
