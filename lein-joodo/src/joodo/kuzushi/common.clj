(ns joodo.kuzushi.common
  (:use
    [leiningen.core :only (read-project)]))

(def endl (System/getProperty "line.separator"))

(defn exit [code]
  (System/exit code))

(defn symbolize [java-map]
  (reduce (fn [result entry] (assoc result (keyword (.getKey entry)) (.getValue entry))) {} java-map))

(defn load-var [ns-sym var-sym]
  (try
    (require ns-sym)
    (let [ns (the-ns ns-sym)]
      (ns-resolve ns var-sym))
    (catch Exception e
      nil)))

(declare #^{:dynamic true} *project*)
(declare #^{:dynamic true} *lib-name*)
(declare #^{:dynamic true} *summary*)

(defn load-lein-project []
  (if-let [project (read-project)]
    project
    (do
      (println "Couldn't find project.clj. Is the current directory a Joodo project?")
      (exit -1))))

(defmacro with-lein-project [& body]
  `(if (bound? #'*project*)
     (do ~@body)
     (binding [*project* (load-lein-project)]
       (do ~@body))))
