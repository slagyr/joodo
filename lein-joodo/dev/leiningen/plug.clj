(ns leiningen.plug
  (:use
    [leiningen.jar :only (jar get-default-jar-name)]
    [leiningen.plugin :only (plugin)]
    [joodo.cmd :only (exec)])
  (:require
    [joodo.kuzushi.version :as version]))

(defn plug [project]
  (println "Installing " version/summary)
  (jar project)
  (exec ["mvn" "install:install-file" "-DgroupId=joodo" "-DartifactId=lein-joodo" (str "-Dversion=" version/string) "-Dpackaging=jar" (str "-Dfile=" (get-default-jar-name project))])
  (plugin "install" "joodo/lein-joodo" version/string)
  (println "Installed " version/summary))



