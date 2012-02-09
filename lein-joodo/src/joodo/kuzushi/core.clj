(ns joodo.kuzushi.core
  (:use
    [joodo.kuzushi.common :only (exit symbolize load-var *command-root*)]
    [joodo.kuzushi.commands.help :as help :only (usage usage-for)])
  (:require
    [joodo.kuzushi.version])
  (:import
    [mmargs Arguments]))

(def arg-spec (Arguments.))
(doto arg-spec
  (.addParameter "command" "The name of the command to execute. Use --help for a listing of command.")
  (.addSwitchOption "v" "version" "Shows the current joodo/kuzushi version.")
  (.addSwitchOption "h" "help" "You're looking at it."))
(reset! joodo.kuzushi.commands.help/main-arg-spec arg-spec)

(defn- resolve-aliases [options]
  (cond
    (:help options) (recur (dissoc (assoc options :command "help") :help))
    (:version options) (recur (dissoc (assoc options :command "version") :version))
    :else options))

(defn parse-args [& args]
  (let [parse-result (.parse arg-spec (into-array String args))
        options (symbolize parse-result)
        options (resolve-aliases options)]
    (if-let [command (:command options)]
      options
      (usage (:*errors options)))))

(defn- load-command [command]
  (let [command-ns-sym (symbol (str *command-root* "." command))
        parse-fn (load-var command-ns-sym 'parse-args)
        exec-fn (load-var command-ns-sym 'execute)]
    (when-not (or parse-fn exec-fn)
      (help/usage [(str "Invalid command: " command)]))
    [parse-fn exec-fn]))

(defn run-command [options]
  (try
    (let [command (:command options)
          [parse-fn exec-fn] (load-command command)
          sub-options (apply parse-fn (:*leftover options))]
      (if-let [errors (:*errors sub-options)]
        (usage-for command errors)
        (exec-fn sub-options)))
    (catch Exception e
      (.printStackTrace e)
      (exit -1))))

(defn run [& args]
  (let [options (apply parse-args args)]
    (println "options: " options)
    (run-command options)
    (exit 0)))

