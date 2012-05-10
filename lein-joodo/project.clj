(def config (load-file "../config.clj"))

(defproject joodo/lein-joodo (:version config)
  :description "Leiningen Plugin for Joodo, a Clojure framework for web apps."
  :license {:name "The MIT License"
            :url "file://LICENSE"
            :distribution :repo
            :comments "Copyright © 2011-2012 Micah Martin All Rights Reserved."}
  :dependencies [[org.clojure/clojure ~(:clojure-version config)]
                 [filecabinet "1.0.4"]
                 [mmargs "1.2.0"]]
  :dev-dependencies [[speclj ~(:speclj-version config)]
                     [filecabinet "1.0.4"]]
  :test-path "spec/"
  :shell-wrapper {:main joodo.kuzushi.main
                  :bin "bin/joodo"}
  :resources-path "resources/"
  :extra-classpath-dirs ["leiningen-1.7.0-standalone.jar"]
  )