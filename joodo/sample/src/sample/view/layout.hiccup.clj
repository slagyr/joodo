(doctype :html5)
[:html
 [:head
  [:meta {:http-equiv "Content-Type" :content "text/html" :charset "iso-8859-1"}]
  [:title "sample"]
  (include-css "/stylesheets/sample.css")
  (include-js "/javascript/sample.js")]
 [:body
  (eval (:template-body *view-context*))]]