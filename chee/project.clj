(let [config (load-file "../config.clj")
      dev-deps [['speclj (:speclj-version config)]]]
  (defproject chee (:version config)
    :description "Support utilities"
    :license {:name "The MIT License"
              :url "file://LICENSE"
              :distribution :repo
              :comments "Copyright (c) 2011-2012 Micah Martin All Rights Reserved."}
    :dependencies [[org.clojure/clojure ~(:clojure-version config)]]
    :dev-dependencies ~dev-deps
    :profiles {:dev {:dependencies ~dev-deps}}
    :plugins ~dev-deps
    :test-path "spec/"
    :test-paths ["spec/"]
    ))