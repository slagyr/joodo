(require `joodo.views)
[:html
 [:head
  [:title "Test Layout"]]
 [:body
  (eval (:template-body joodo.views/*view-context*))]]