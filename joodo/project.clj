(defproject joodo "2.1.0"
  :description "Joodo, a Clojure framework for web apps."
  :license {:name "The MIT License"
            :url "file://LICENSE"
            :distribution :repo
            :comments "Copyright (c) 2011-2013 Micah Martin All Rights Reserved."}
  :dependencies [[chee "2.0.0"]
                 [com.taoensso/timbre "2.6.3"]
                 [compojure "1.1.6"]
                 [digest "1.4.3"]
                 [hiccup "1.0.4"]
                 [org.clojure/clojure "1.5.1"]
                 [ring/ring-core "1.2.1"]]
  :profiles {:dev {:dependencies [[speclj "2.9.0"]]}}
  :plugins [[speclj "2.9.0"]]
  :test-paths ["spec/"]
  :java-source-paths ["src/"]
  )
