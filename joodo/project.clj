(defproject joodo "0.6.3-SNAPSHOT"
  :description "Joodo, a Clojure framework for web apps."
  :license {:name "The MIT License"
            :url "file://LICENSE"
            :distribution :repo
            :comments "Copyright © 2011 Micah Martin All Rights Reserved."}
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [ring/ring-core "0.3.8"]
                 [ring/ring-jetty-adapter "0.3.8"]
                 [compojure "1.0.0"]
                 [hiccup "0.3.1"]
                 [mmargs "1.2.0"]]
  :dev-dependencies [[speclj "1.5.1"]
                     [speclj-growl "1.0.0-SNAPSHOT"]]
  :test-path "spec/"
  :java-source-path "src/")
