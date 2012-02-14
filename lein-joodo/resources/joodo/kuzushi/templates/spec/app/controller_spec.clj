(ns !-CONTROLLER_NS-!-spec
  (:use
    [speclj.core]
    [joodo.spec-helpers.controller]
    [!-CONTROLLER_NS-!]))

(describe "!-CONTROLLER_SUBJECT-! Controller"

  (with-mock-rendering)
  (with-routes !-CONTROLLER-NAME-!)

  (it "has default route"
    (let [response (do-get "/!-CONTROLLER-SUBJECT-!/test")]
      (should= 200 (:status invalid_doc))
      (should= "PASS" (:body response))
      (should= nil @rendered-template)))
  )
)

(run-specs)