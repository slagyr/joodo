(ns leiningen.replug
  (:use
    [joodo.cmd :only (exec)]))

(defn replug [project]
  (exec ["lein" "unplug"])
  (exec ["lein" "plug"]))
