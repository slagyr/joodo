(ns joodo.kuzushi.main
  (:use
    [joodo.kuzushi.core :only (run)]
    [joodo.kuzushi.common :only (*project* *lib-name* *summary* load-lein-project)])
  (:require
    [joodo.kuzushi.version]))


(defn- run-with-bindings [args]
  (binding [*summary* (str joodo.kuzushi.version/summary ": Command line component for Joodo; A Clojure framework for web applications.")
            *lib-name* "joodo"]
    (apply run args)))

(defn run-with-project [project & args]
  (binding [*project* project]
    (run-with-bindings args)))

(defn run-with-args [& args]
  (if (= "new" (first args))
    (run-with-bindings args)
    (binding [*project* (load-lein-project)]
      (run-with-bindings args))))

(defn -main [& args]
  (run-with-bindings args))