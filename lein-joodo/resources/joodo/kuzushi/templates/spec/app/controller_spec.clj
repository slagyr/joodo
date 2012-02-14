(ns !-CONTROLLER_NS-!-spec
  (:use
    [speclj.core]
    [joodo.spec-helpers.controller]
    [!-CONTROLLER_NS-!]))

(describe "!-CONTROLLER_SUBJECT-! Controller"

  (with-mock-rendering)
  (with-routes !-CONTROLLER_NAME-!)

  (it "has test route"
    (let [response (do-get "/!-CONTROLLER_SUBJECT-!/test")]
      (should= 200 (:status response))
      (should= "PASS" (:body response))
      (should= nil @rendered-template)
      (should= nil @rendered-context)))
  )

(run-specs)