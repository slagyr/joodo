(ns leiningen.joodo
  (:use
    [joodo.kuzushi.main :only (run-with-project)]))

(defn joodo [project & args]
  (apply run-with-project project args))