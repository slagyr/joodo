(ns leiningen.replug
  (:use
    [gaeshi.cmd :only (exec)]))

(defn replug [project]
  (exec ["lein" "unplug"])
  (exec ["lein" "plug"]))
