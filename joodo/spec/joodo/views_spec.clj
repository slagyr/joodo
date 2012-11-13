(ns joodo.views-spec
  (:use
    [speclj.core]
    [joodo.views])
  (:import
    [java.io StringReader]))

(defmacro should-contain [doc snip]
  `(let [index# (.indexOf ~doc ~snip)]
     (if (= -1 index#)
       (throw (speclj.SpecFailure. (str "the document doesn't contain the snip: " ~snip))))))

(def foo "FOO")

(describe "Views"

  (it "default context"
    (should= "layout" (:layout *view-context*))
    (should= "view" (:template-root *view-context*))
    (should= `joodo.default-rendering (:ns *view-context*)))

  (it "render without a layout"
    (should= "No Layout" (render-html "No Layout" :layout false))
    (should= "No Layout" (render-html "No Layout" :layout nil)))

  (it "can change the rendering ns"
    (should= "<p>FOO</p>" (render-hiccup `[:p foo] :layout false :ns 'joodo.test-view.context))
    (should= "<p>FOO</p>" (render-hiccup `[:p foo] :layout false :ns "joodo.test-view.context")))

  (it "values can be easily added to view context"
    (should= "<p>My Value</p>" (render-hiccup `[:p (:my-value *view-context*)] :my-value "My Value" :layout false)))

  (it "can set a new template root"
    (let [html (render-template "test_template" :template-root "joodo/test_view")]
      (should-contain html "<title>Test Layout</title>")
      (should-contain html "<body><b>Test Template</b></body>")))

  (context "with test templates"

    (around [it]
      (binding [*view-context* (assoc *view-context* :template-root "joodo/test_view")]
        (it)))

    (it "renders main layout"
      (let [html (render-html "")]
        (should-contain html "<title>Test Layout</title>")))

    (it "renders main layout with content as body"
      (let [html (render-html "My Content")]
        (should-contain html "<body>My Content</body>")))

    (it "renders layout without .clj extension"
      (binding [*view-context* (assoc *view-context* :layout "layout2")]
        (let [html (render-html "My Content")]
          (should-contain html "<title>Test Layout2</title>")
          (should-contain html "<body>My Content</body>"))))

    (it "renders hiccup content"
      (let [html (render-hiccup `[:div "Hiccup!"])]
        (should-contain html "<title>Test Layout</title>")
        (should-contain html "<body><div>Hiccup!</div></body>")))

    (it "renders a template"
      (let [html (render-template "test_template")]
        (should-contain html "<title>Test Layout</title>")
        (should-contain html "<body><b>Test Template</b></body>")))

    (it "renders a template with no .clj extension"
      (let [html (render-template "test_template2")]
        (should-contain html "<title>Test Layout</title>")
        (should-contain html "<body><i>Test Template2</i></body>")))

    (it "renders nested template"
      (let [html (render-template "nested/nested_template")]
        (should-contain html "<title>Test Layout</title>")
        (should-contain html "<body><a>Nested Template</a></body>")))

    (it "renders partials"
      (let [html (render-hiccup `(render-partial "test_partial"))]
        (should-contain html "<title>Test Layout</title>")
        (should-contain html "<body><span>Test Partial</span></body>")))

    (it "renders nested partial"
      (let [html (render-hiccup `(render-partial "nested/nested_partial"))]
        (should-contain html "<title>Test Layout</title>")
        (should-contain html "<body><p>Nested Partial</p></body>")))

    (it "provides nice error when template is missing"
      (should-throw
        Exception
        "Template Not Found: joodo/test_view/non-existent.hiccup[.clj]"
        (render-template "non-existent")))

    (it "provides nice error when layout is missing"
      (binding [*view-context* (assoc *view-context* :layout "missing-layout")]
        (should-throw
          Exception
          "Template Not Found: joodo/test_view/missing-layout.hiccup[.clj]"
          (render-template "test_template"))))))

(run-specs)



