(ns joodo.env-spec
  (:use
    [speclj.core]
    [joodo.env]))

(describe "Env"

  (it "defaults to development joodo env"
    (should= true (development-env?))
    (should= false (production-env?))
    (should= "development" (env :joodo-env))
    (should= "development" (env "joodo-env")))

  (it "can change to production env"
    (swap! *env* assoc :joodo-env "production")
    (should= false (development-env?))
    (should= true (production-env?)))

  (it "reads in all the system properties"
    (doseq [[key value] (System/getProperties)]
      (should= value (env key))))
  )

(run-specs)

