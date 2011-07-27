(ns harai.core-spec
  (:use
    [speclj.core]
    [gaeshi.spec-helpers.controller]
    [harai.core]))

(describe "harai"

  (with-mock-rendering)
  (with-routes app-handler)

  (it "handles home page"
    (let [result (do-get "/")]
      (should= 200 (:status result))
      (should= "index" @rendered-template)))
  )

(run-specs)
