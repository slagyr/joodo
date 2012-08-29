(let [config (load-file "../config.clj")]
  (defproject chee (:version config)
    :description "Support utilities"
    :license {:name "The MIT License"
              :url "file://LICENSE"
              :distribution :repo
              :comments "Copyright (c) 2011-2012 Micah Martin All Rights Reserved."}
    :dependencies [[org.clojure/clojure ~(:clojure-version config)]]
    :profiles {:dev {:dependencies [[speclj ~(:speclj-version config)]]}}
    :plugins [[speclj ~(:speclj-version config)]]
    :test-paths ["spec/"]
    ))