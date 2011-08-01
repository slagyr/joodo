(ns joodo.kuzushi.commands.version
  (:use
    [joodo.kuzushi.common :only (exit symbolize)])
  (:require
    [joodo.kuzushi.version])
  (:import
    [mmargs Arguments]))

(def arg-spec (Arguments.))

(defn parse-args [& args]
  (symbolize (.parse arg-spec (into-array String args))))

(defn execute
  "Prints the current version of joodo/kuzushi"
  [options]
  (println joodo.kuzushi.version/summary)
  (exit 0))

