(ns leiningen.joodo
  (:require [joodo.kuzushi.main]))

(defn ^:no-project-needed joodo [project & args]
  (apply joodo.kuzushi.main/run-with-args args))