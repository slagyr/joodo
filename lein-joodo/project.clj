(defproject joodo/lein-joodo "1.1.2"
  :description "Leiningen Plugin for Joodo, a Clojure framework for web apps."
  :license {:name "The MIT License"
            :url "file://LICENSE"
            :distribution :repo
            :comments "Copyright (c) 2011-2013 Micah Martin All Rights Reserved."}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [filecabinet "1.0.4"]
                 [mmargs "1.2.0"]]
  :profiles {:dev {:dependencies [[speclj "2.5.0"]
                                  [filecabinet "1.0.4"]]}}
  :plugins [[speclj "2.5.0"]]
  :test-paths ["spec/"]
  :shell-wrapper {:main joodo.kuzushi.main
                  :bin "bin/joodo"}
  :resource-paths ["resources/"]
  )
