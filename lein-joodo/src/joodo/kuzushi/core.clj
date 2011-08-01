(ns joodo.kuzushi.core
  (:use
    [joodo.kuzushi.common :only (exit symbolize load-var)]
    [joodo.kuzushi.commands.help :only (usage usage-for)])
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

(defn run-command [options]
  (try
    (let [command (:command options)
          command-ns-sym (symbol (str "joodo.kuzushi.commands." command))
          parse-fn (load-var command-ns-sym 'parse-args)
          sub-options (apply parse-fn (:*leftover options))]
      (if-let [errors (:*errors sub-options)]
        (usage-for command errors)
        (let [exec-fn (load-var command-ns-sym 'execute)]
          (exec-fn sub-options))))
    (catch Exception e
      (.printStackTrace e)
      (exit -1))))


(defn run [& args]
  (let [options (apply parse-args args)]
    (run-command options)
    (exit 0)))

(defn -main [& args]
  (apply run args))

