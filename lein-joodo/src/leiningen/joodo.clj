(ns leiningen.joodo
  (:use
    [joodo.kuzushi.core :only (run-with-project)]))

(defn joodo [project & args]
  (apply run-with-project project args))