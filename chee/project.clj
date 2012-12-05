(defproject chee "1.1.0"
  :description "Support utilities"
  :license {:name "The MIT License"
            :url "file://LICENSE"
            :distribution :repo
            :comments "Copyright (c) 2011-2012 Micah Martin All Rights Reserved."}
  :dependencies [[org.clojure/clojure "1.4.0"]]
  :profiles {:dev {:dependencies [[speclj "2.3.4"]]}}
  :plugins [[speclj "2.3.4"]]
  :test-paths ["spec/"]
  )