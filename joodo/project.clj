(defproject joodo "1.0.0"
  :description "Joodo, a Clojure framework for web apps."
  :license {:name "The MIT License"
            :url "file://LICENSE"
            :distribution :repo
            :comments "Copyright (c) 2011-2012 Micah Martin All Rights Reserved."}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [ring/ring-core "1.1.6"]
                 [ring/ring-jetty-adapter "1.1.6"]
                 [compojure "1.1.3"]
                 [hiccup "1.0.1"]
                 [mmargs "1.2.0"]
                 [filecabinet "1.0.4"]
                 [chee "1.0.0"]
                 [org.clojure/tools.logging "0.2.4"]]
  :profiles {:dev {:dependencies [[speclj "2.3.1"]]}}
  :plugins [[speclj "2.3.1"]]
  :test-paths ["spec/"]
  :java-source-paths ["src/"]
  )