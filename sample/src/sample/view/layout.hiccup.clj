(doctype :html5)
[:html
 [:head
  [:meta {:http-equiv "Content-Type" :content "text/html" :charset "iso-8859-1"}]
  [:title "sample"]
  (include-css "/stylesheets/sample.css")
  (include-js "/javascript/sample.js")]
 [:body
  [:h1 "HEY!!!"]
  (:template-body joodo.views/*view-context*)
  (prn (:template-body joodo.views/*view-context*))
  (prn joodo.views/*view-context*)
  (eval (:template-body joodo.views/*view-context*))
]]