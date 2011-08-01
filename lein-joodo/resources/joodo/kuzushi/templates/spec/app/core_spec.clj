(ns !-APP_NAME-!.core-spec
  (:use
    [speclj.core]
    [joodo.spec-helpers.controller]
    [!-APP_NAME-!.core]))

(describe "!-APP_NAME-!"

  (with-mock-rendering)
  (with-routes app-handler)

  (it "handles home page"
    (let [result (do-get "/")]
      (should= 200 (:status result))
      (should= "index" @rendered-template)))
  )

(run-specs)
