(defproject !-APP_NAME-! "0.0.1"
  :description "A website deployable to AppEngine"
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [joodo/kake "!-KAKE_VERSION-!"]]
  :dev-dependencies [[joodo/tsukuri "!-TSUKURI_VERSION-!"]
                     [speclj "1.4.0"]]
  :test-path "spec/"
  :java-source-path "src/"
  :repl-init-script "config/development/repl_init.clj"
  :joodo-core-namespace !-APP_NAME-!.core)