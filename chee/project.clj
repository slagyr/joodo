(defproject chee "2.0.0"
  :description "Support utilities"
  :license {:name "The MIT License"
            :url "file://LICENSE"
            :distribution :repo
            :comments "Copyright (c) 2011-2013 Micah Martin All Rights Reserved."}
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :profiles {:dev {:dependencies [[speclj "2.8.1"]]}}
  :plugins [[speclj "2.8.1"]]
  :test-paths ["spec/"]
  )