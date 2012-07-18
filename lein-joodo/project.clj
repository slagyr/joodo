(def config
  (try
    (load-file "../config.clj")
    (catch java.io.FileNotFoundException e
      {:version "0.9.0"
       :clojure-version "1.4.0"
       :speclj-version "2.2.0"})))

(let [dev-deps [['speclj (:speclj-version config)]
                ['filecabinet "1.0.4"]]]
  (defproject joodo/lein-joodo (:version config)
    :description "Leiningen Plugin for Joodo, a Clojure framework for web apps."
    :license {:name "The MIT License"
              :url "file://LICENSE"
              :distribution :repo
              :comments "Copyright 2011-2012 Micah Martin All Rights Reserved."}
    :dependencies [[org.clojure/clojure ~(:clojure-version config)]
                   [filecabinet "1.0.4"]
                   [mmargs "1.2.0"]
                   [leiningen "2.0.0-preview7"]]
    :dev-dependencies ~dev-deps
    :profiles {:dev {:dependencies ~dev-deps}}
    :plugins ~dev-deps
    :test-path "spec/"
    :test-paths ["spec/"]
    :shell-wrapper {:main joodo.kuzushi.main
                    :bin "bin/joodo"}
    :resources-path "resources/"
;    :extra-classpath-dirs ["leiningen-1.7.0-standalone.jar"]
    ))