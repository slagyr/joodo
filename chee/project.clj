(def config (load-file "../config.clj"))

(defproject chee (:version config)
  :description "Support utilities"
  :dependencies [[org.clojure/clojure ~(:clojure-version config)]]
  :dev-dependencies [[speclj ~(:speclj-version config)]]
  :test-path "spec/")