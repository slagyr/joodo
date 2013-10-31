(ns {{name}}.main-spec
  (:require [speclj.core :refer :all]
            [joodo.spec-helpers.controller :refer :all]
            [{{name}}.main :refer :all]))

(describe "{{name}}"

  (with-mock-rendering)
  (with-routes app-handler)

  (it "handles root"
    (let [result (do-get "/")]
      (should= 200 (:status result))
      (should= "index" @rendered-template)))
  )

(run-specs)
