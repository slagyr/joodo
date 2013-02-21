(ns joodo.kuzushi.commands.server
  (:require [joodo.cmd :refer [java]]
            [joodo.kuzushi.common :refer [symbolize with-lein-project *project* read-project]]
            [clojure.string :as str])
  (:import [mmargs Arguments]))

(def arg-spec (Arguments.))
(doto arg-spec
  (.addValueOption "p" "port" "PORT" "Change the port (default: 8080)")
  (.addValueOption "a" "address" "ADDRESS" "Change the address (default: 0.0.0.0)")
  (.addValueOption "e" "environment" "ENVIRONMENT" "Change the environment (default: development)"))

(def default-options {:port 8080
                      :address "0.0.0.0"
                      :environment "development"})

(defn parse-args [& args]
  (let [options (symbolize (.parse arg-spec (into-array String args)))
        options (if (contains? options :port ) (assoc options :port (Integer/parseInt (:port options))) options)]
    (merge default-options options)))

(defn get-classpath [project]
  (try
    (require 'leiningen.core.classpath)
    (str/join java.io.File/pathSeparatorChar ((ns-resolve 'leiningen.core.classpath 'get-classpath) project))
    (catch java.io.FileNotFoundException e
      (require 'leiningen.classpath)
      ((ns-resolve 'leiningen.classpath 'get-classpath-string) project))))

(defn- java-cmd [jvm-args main-class args working-directory]
  (let [java-exe (str (System/getProperty "java.home") "/bin/java")
        command (into-array (map str (concat [java-exe] jvm-args [main-class] args)))]
    (str/join " " command)))

(defn execute
  "Starts the app in a local web server"
  [options]
  (with-lein-project
    (let [classpath (get-classpath *project*)
          jvm-args ["-cp" classpath]
          args ["-m" "joodo.main" "port" (:port options) "host" (:address options) "environment" (:environment options)]
          cmd (java-cmd jvm-args "clojure.main" args ".")]
      (if-let [trampoline-file (or (System/getProperty "leiningen.trampoline-file")
                                   (System/getenv "TRAMPOLINE_FILE"))]
        (spit trampoline-file cmd)
        (println "No trampoline file specified, so the Joodo server could not start.")))))

