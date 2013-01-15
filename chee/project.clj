(defproject chee "1.1.1"
  :description "Support utilities"
  :license {:name "The MIT License"
            :url "file://LICENSE"
            :distribution :repo
            :comments "Copyright (c) 2011-2013 Micah Martin All Rights Reserved."}
  :dependencies [[org.clojure/clojure "1.4.0"]]
  :profiles {:dev {:dependencies [[speclj "2.5.0"]]}}
  :plugins [[speclj "2.5.0"]]
  :test-paths ["spec/"]
  )