(defproject joodo "2.0.0"
  :description "Joodo, a Clojure framework for web apps."
  :license {:name "The MIT License"
            :url "file://LICENSE"
            :distribution :repo
            :comments "Copyright (c) 2011-2013 Micah Martin All Rights Reserved."}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring/ring-core "1.1.8"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.4"]
                 [chee "2.0.0"]]
  :profiles {:dev {:dependencies [[speclj "2.6.1"]]}}
  :plugins [[speclj "2.6.1"]]
  :test-paths ["spec/"]
  :java-source-paths ["src/"]
  )
