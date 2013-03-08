(defproject !-APP_NAME-! "0.0.1"
  :description "A simple stand-alone webapp"
  :dependencies [[org.clojure/clojure "1.5.0"]
                 [joodo "!-JOODO_VERSION-!"]]
  :min-lein-version "2.0.0"

  :joodo-root-namespace !-APP_NAME-!.root

  :profiles {:dev {:dependencies [[speclj "2.5.0"]]}}
  :test-paths ["spec/"]
  :java-source-paths ["src/"]
  :plugins [[speclj "2.5.0"]
            [joodo/lein-joodo "!-JOODO_VERSION-!"]]

  )
