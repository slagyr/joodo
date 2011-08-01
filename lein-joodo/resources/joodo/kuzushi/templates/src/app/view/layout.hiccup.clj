(doctype :html5)
[:html
 [:head
  [:meta {:http-equiv "Content-Type" :content "text/html" :charset "iso-8859-1"}]
  [:title "!-APP_NAME-!"]
  (include-css "/stylesheets/!-APP_NAME-!.css")
  (include-js "/javascript/!-APP_NAME-!.js")]
 [:body
  (eval (:template-body joodo.views/*view-context*))
]]