(ns joodo.views-spec
  (:use
    [speclj.core]
    [joodo.views])
  (:import
    [java.io StringReader]))

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
      (should-contain "<title>Test Layout</title>" html)
      (should-contain "<body><b>Test Template</b></body>" html)))

  (context "with test templates"

    (around [it]
      (binding [*view-context* (assoc *view-context* :template-root "joodo/test_view")]
        (it)))

    (it "renders main layout"
      (let [html (render-html "")]
        (should-contain "<title>Test Layout</title>" html)))

    (it "renders main layout with content as body"
      (let [html (render-html "My Content")]
        (should-contain "<body>My Content</body>" html)))

    (it "renders layout without .clj extension"
      (binding [*view-context* (assoc *view-context* :layout "layout2")]
        (let [html (render-html "My Content")]
          (should-contain "<title>Test Layout2</title>" html)
          (should-contain "<body>My Content</body>" html))))

    (it "renders hiccup content"
      (let [html (render-hiccup `[:div "Hiccup!"])]
        (should-contain "<title>Test Layout</title>" html)
        (should-contain "<body><div>Hiccup!</div></body>" html)))

    (it "renders a template"
      (let [html (render-template "test_template")]
        (should-contain "<title>Test Layout</title>" html)
        (should-contain "<body><b>Test Template</b></body>" html)))

    (it "renders a template with no .clj extension"
      (let [html (render-template "test_template2")]
        (should-contain "<title>Test Layout</title>" html)
        (should-contain "<body><i>Test Template2</i></body>" html)))

    (it "renders nested template"
      (let [html (render-template "nested/nested_template")]
        (should-contain "<title>Test Layout</title>" html)
        (should-contain "<body><a>Nested Template</a></body>" html)))

    (it "renders partials"
      (let [html (render-hiccup `(render-partial "test_partial"))]
        (should-contain "<title>Test Layout</title>" html)
        (should-contain "<body><span>Test Partial</span></body>" html)))

    (it "renders nested partial"
      (let [html (render-hiccup `(render-partial "nested/nested_partial"))]
        (should-contain "<title>Test Layout</title>" html)
        (should-contain "<body><p>Nested Partial</p></body>" html)))

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



