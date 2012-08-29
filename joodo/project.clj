(let [config (load-file "../config.clj")]
  (defproject joodo (:version config)
    :description "Joodo, a Clojure framework for web apps."
    :license {:name "The MIT License"
              :url "file://LICENSE"
              :distribution :repo
              :comments "Copyright (c) 2011-2012 Micah Martin All Rights Reserved."}
    :dependencies [[org.clojure/clojure ~(:clojure-version config)]
                   [ring/ring-core "1.0.2"]
                   [ring/ring-jetty-adapter "1.0.2"]
                   [compojure "1.0.1"]
                   [hiccup "0.3.8"]
                   [mmargs "1.2.0"]
                   [filecabinet "1.0.4"]
                   [chee ~(:version config)]]

    :profiles {:dev {:dependencies [[speclj ~(:speclj-version config)]]}}
    :plugins [[speclj ~(:speclj-version config)]]
    :test-paths ["spec/"]
    :java-source-paths ["src/"]
    ))