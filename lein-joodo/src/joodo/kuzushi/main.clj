(ns joodo.kuzushi.main
  (:use
    [joodo.kuzushi.core :only (run)]
    [joodo.kuzushi.common :only (*project* *command-root* *main-name*)]))


(defn- run-with-bindings [args]
  (binding [*command-root* "joodo.kuzushi.commands"
            *main-name* "joodo"]
    (apply run args)))

(defn run-with-project [project & args]
  (binding [*project* project]
    (run-with-bindings args)))

(defn -main [& args]
  (run-with-bindings args))