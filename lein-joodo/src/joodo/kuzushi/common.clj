(ns joodo.kuzushi.common)

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

(defn read-project []
  (try
    (require 'leiningen.core.project)
    ((ns-resolve 'leiningen.core.project 'read))
    (catch java.io.FileNotFoundException e
      (require 'leiningen.core)
      ((ns-resolve 'leiningen.core 'read-project)))))

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

(defn ->options
  "Takes keyword argument and converts them to a map.  If the args are prefixed with a map, the rest of the
  args are merged in."
  [options]
  (if (map? (first options))
    (merge (first options) (apply hash-map (rest options)))
    (apply hash-map options)))
