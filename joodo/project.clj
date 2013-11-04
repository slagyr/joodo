(defproject joodo "1.2.3"
  :description "Joodo, a Clojure framework for web apps."
  :license {:name "The MIT License"
            :url "file://LICENSE"
            :distribution :repo
            :comments "Copyright (c) 2011-2013 Micah Martin All Rights Reserved."}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring/ring-core "1.1.7"]
                 [ring/ring-jetty-adapter "1.1.7"]
                 [compojure "1.1.5"]
                 [hiccup "1.0.2"]
                 [mmargs "1.2.0"]
                 [filecabinet "1.0.4"]
                 [chee "1.1.2"]]
  :profiles {:dev {:dependencies [[speclj "2.6.1"]]}}
  :plugins [[speclj "2.6.1"]]
  :test-paths ["spec/"]
  :java-source-paths ["src/"]
  )
