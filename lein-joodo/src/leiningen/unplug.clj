(ns leiningen.unplug
  (:use
    [leiningen.plugin :only (plugin)])
  (:require
    [gaeshi.kuzushi.version :as version]))

(defn unplug [project]
  (println "Uninstalling" version/summary)
  (try
    (plugin "uninstall" "gaeshi/kuzushi" version/string)
    (catch Exception e
      (println "failed to uninstall plugin:" e))))
