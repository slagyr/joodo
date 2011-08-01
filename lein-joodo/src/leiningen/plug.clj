(ns leiningen.plug
  (:use
    [leiningen.jar :only (jar get-default-jar-name)]
    [leiningen.plugin :only (plugin)]
    [gaeshi.cmd :only (exec)])
  (:require
    [gaeshi.kuzushi.version :as version]))

(defn plug [project]
  (println "Installing " version/summary)
  (jar project)
  (exec ["mvn" "install:install-file" "-DgroupId=gaeshi" "-DartifactId=kuzushi" (str "-Dversion=" version/string) "-Dpackaging=jar" (str "-Dfile=" (get-default-jar-name project))])
  (plugin "install" "gaeshi/kuzushi" version/string)
  (println "Installed " version/summary))



