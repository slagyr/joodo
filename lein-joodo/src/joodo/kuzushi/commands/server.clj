(ns joodo.kuzushi.commands.server
  (:use
    [leiningen.core :only (read-project)]
    [leiningen.clean :only (clean)]
    [leiningen.classpath :only (get-classpath-string)]
    [joodo.cmd :only (java)]
    [joodo.kuzushi.common :only (symbolize with-lein-project *project*)])
  (:import
    [mmargs Arguments]))

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
        options (if (contains? options :port) (assoc options :port (Integer/parseInt (:port options))) options)]
    (merge default-options options)))

(defn execute
  "Starts the app in on a local web server"
  [options]
  (with-lein-project
    (let [classpath (get-classpath-string *project*)
          jvm-args ["-cp" classpath]
          args ["-p" (:port options) "-a" (:address options) "-e" (:environment options) "-d" (:directory options)]]
      (java jvm-args "joodo.kake.JoodoServer" (map str args)))))

