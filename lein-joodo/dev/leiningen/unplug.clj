(ns leiningen.unplug
  (:use
    [leiningen.plugin :only (plugin)])
  (:require
    [joodo.kuzushi.version :as version]))

(defn unplug [project]
  (println "Uninstalling" version/summary)
  (try
    (plugin "uninstall" "joodo/lein-joodo" version/string)
    (catch Exception e
      (println "failed to uninstall plugin:" e))))
