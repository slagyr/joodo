(defproject joodo "0.0.1-SNAPSHOT"
  :description "Clojure Web App Library"
  :license {:name "The MIT License"
            :url "file://LICENSE"
            :distribution :repo
            :comments "Copyright © 2011 Micah Martin All Rights Reserved."}
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [ring/ring-core "0.3.8"]
                 [ring/ring-jetty-adapter "0.3.8"]
                 [compojure "0.6.2"]
                 [hiccup "0.3.1"]]
  :dev-dependencies [[speclj "1.4.0"]
                     [lein-clojars "0.6.0"]]
  :test-path "spec/"
  :java-source-path "src/")