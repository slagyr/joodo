(ns joodo.env-spec
  (:require [speclj.core :refer :all]
            [joodo.env :refer :all]
            [joodo.spec-helper]))

(describe "Env"

  (it "defaults to development joodo env"
    (alter-env! assoc :joodo-env "development")
    (should= true (development?))
    (should= false (production?))
    (should= "development" (env :joodo-env ))
    (should= "development" (env "joodo-env")))

  (it "can change to production env"
    (alter-env! assoc :joodo-env "production")
    (should= false (development?))
    (should= true (production?)))

  (it "reads in all the system properties"
    (doseq [[key value] (System/getProperties)]
      (should= value (env key))))

  (context "with fs"

    (with fs (atom {}))
    (around [it]
      (with-redefs [slurp (fn [path] (get @@fs path))]
        (it)))
    (after (System/setProperty "joodo.env" "development")
           (System/setProperty "joodo.ignore.config" "false"))

    (it "loads a config file"
      (swap! @fs assoc "config/test.clj" "(alter-env! assoc :env-conf *ns*)")
      (System/setProperty "joodo.env" "test")
      (load-configurations)
      (should-not= nil (env :env-conf ))
      (should= true (.startsWith (name (.getName (env :env-conf ))) "joodo.config-")))

    (it "loads a config file from env dir"
      (swap! @fs assoc "config/test.clj" "(alter-env! assoc :env-conf *ns*)")
      (System/setProperty "joodo.env" "test")
      (System/setProperty "joodo.ignore.config" "true")
      (set-env! {:joodo.root.namespace "joodo.test-override-core"})
      (load-configurations)
      (should= nil (env :env-conf)))
    )

  )

(run-specs)

