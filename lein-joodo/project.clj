(defproject joodo/lein-joodo "0.7.2"
  :description "Leiningen Plugin for Joodo, a Clojure framework for web apps."
  :license {:name "The MIT License"
            :url "file://LICENSE"
            :distribution :repo
            :comments "Copyright © 2011-2012 Micah Martin All Rights Reserved."}
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [filecabinet "1.0.4"]
                 [mmargs "1.2.0"]]
  :dev-dependencies [[speclj "2.1.1"]
                     [filecabinet "1.0.4"]]
  :test-path "spec/"
  :shell-wrapper {:main joodo.kuzushi.main
                  :bin "bin/joodo"}
  :resources-path "resources/"
  :extra-classpath-dirs ["leiningen-1.7.0-standalone.jar"]
  )