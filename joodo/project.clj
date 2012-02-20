(defproject joodo "0.7.1"
  :description "Joodo, a Clojure framework for web apps."
  :license {:name "The MIT License"
            :url "file://LICENSE"
            :distribution :repo
            :comments "Copyright © 2011-2012 Micah Martin All Rights Reserved."}
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [ring/ring-core "1.0.2"]
                 [ring/ring-jetty-adapter "1.0.2"]
                 [compojure "1.0.1"]
                 [hiccup "0.3.8"]
                 [mmargs "1.2.0"]
                 [filecabinet "1.0.4"]]
  :dev-dependencies [[speclj "2.1.1"]]
  :test-path "spec/"
  :java-source-path "src/")
