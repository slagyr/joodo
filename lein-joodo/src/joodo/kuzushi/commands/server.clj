(ns joodo.kuzushi.commands.server
  (:require [clojure.string]
            [joodo.cmd :refer [java]]
            [joodo.kuzushi.common :refer [symbolize with-lein-project *project* read-project]])
  (:import [mmargs Arguments]))

(def arg-spec (Arguments.))
(doto arg-spec
  (.addValueOption "p" "port" "PORT" "Change the port (default: 8080)")
  (.addValueOption "a" "address" "ADDRESS" "Change the address (default: 127.0.0.1)")
  (.addValueOption "e" "environment" "ENVIRONMENT" "Change the environment (default: development)")
  (.addValueOption "d" "directory" "DIRECTORY" "Change the directory (default: .)"))

(def default-options {
                       :port 8080
                       :address "127.0.0.1"
                       :environment "development"
                       :directory "."})

(defn parse-args [& args]
  (let [options (symbolize (.parse arg-spec (into-array String args)))
        options (if (contains? options :port ) (assoc options :port (Integer/parseInt (:port options))) options)]
    (merge default-options options)))

(defn get-classpath [project]
  (try
    (require 'leiningen.core.classpath)
    (clojure.string/join java.io.File/pathSeparatorChar ((ns-resolve 'leiningen.core.classpath 'get-classpath) project))
    (catch java.io.FileNotFoundException e
      (require 'leiningen.classpath)
      ((ns-resolve 'leiningen.classpath 'get-classpath-string) project))))

(defn execute
  "Starts the app in on a local web server"
  [options]
  (with-lein-project
    (let [classpath (get-classpath *project*)
          jvm-args ["-cp" classpath]
          args ["-p" (:port options) "-a" (:address options) "-e" (:environment options) "-d" (:directory options)]]
      (java jvm-args "joodo.kake.JoodoServer" (map str args)))))

