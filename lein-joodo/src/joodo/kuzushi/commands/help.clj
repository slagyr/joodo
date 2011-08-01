(ns joodo.kuzushi.commands.help
  (:use
    [joodo.kuzushi.common :only (exit symbolize load-var)])
  (:require
    [clojure.string :as str]
    [joodo.kuzushi.version])
  (:import
    [mmargs Arguments]
    [filecabinet FileSystem]))

(def main-arg-spec (atom nil))

(def arg-spec (Arguments.))
(doto arg-spec
  (.addOptionalParameter "command" "Get help for this command"))

(defn parse-args [& args]
  (symbolize (.parse arg-spec (into-array String args))))

(defn find-resource [& names]
  (some
    (fn [name] (.getResource (clojure.lang.RT/baseLoader) name))
    names))

(def ns-regex #"^(.*)(?:(?:\.clj)|(?:__init\.class))$")

(defn extract-ns-from-filename [filename]
  (if-let [groups (first (re-seq ns-regex filename))]
    (second groups)
    nil))

(defn all-commands []
  (let [help-src-url (find-resource "joodo/kuzushi/commands/help.clj" "joodo/kuzushi/commands/help__init.class")
        commands-dir (.parentPath (FileSystem/instance) (.toString help-src-url))
        files (.fileListing (FileSystem/instance) commands-dir)]
    (sort (filter identity (map extract-ns-from-filename files)))))

(defn- docstring-for [command]
  (if-let [exec-fn (load-var (symbol (str "joodo.kuzushi.commands." command)) 'execute)]
    (:doc (meta exec-fn))
    (do
      (println "Failed to load command:" command)
      (exit -1))))

(defn- print-commands []
  (println "  Commands:")
  (let [commands (all-commands)
        docstrings (map docstring-for commands)
        command-ary (into-array String commands)
        doc-ary (into-array String docstrings)]
    (println (Arguments/tabularize command-ary doc-ary))))

(defn- print-errors [errors]
  (when (seq errors)
    (println "ERROR!!!")
    (doseq [error (seq errors)]
      (println error))))

(defn- exit-with [errors]
  (if (seq errors)
    (exit -1)
    (exit 0)))

(defn usage [errors]
  (print-errors errors)
  (println)
  (println joodo.kuzushi.version/summary ": Command line component for Gaeshi; A Clojure framework to build AppEngine web applications.")
  (println)
  (println "Usage: joodo" (.argString @main-arg-spec) "[command options]")
  (println)
  (println (.parametersString @main-arg-spec))
  (println (.optionsString @main-arg-spec))
  (print-commands)
  (exit-with errors))

(defn usage-for [command errors]
  (let [docstring (docstring-for command)
        arg-spec @(load-var (symbol (str "joodo.kuzushi.commands." command)) 'arg-spec)]
    (print-errors errors)
    (println)
    (println command ":" docstring)
    (println)
    (println "Usage: joodo" command (.argString arg-spec))
    (println)
    (println (.parametersString arg-spec))
    (println (.optionsString arg-spec))
    (exit-with errors)))

(defn execute
  "Prints help message for commands: joodo help <command>"
  [options]
  (try
    (if-let [command (:command options)]
      (usage-for command nil)
      (usage nil))
    (catch Exception e
      (println "Sorry, I can't help you with that. " e)
      (exit -1))))