(ns leiningen.oh-snap
  "Finds all snapshot libraries with crazy names and rename them to end with -SNAPSHOT.jar"
  (:import
    [java.io File]))

(def bad-snapshot #"(.*\-)(\d{8}\.\d+\-\d+.jar)")

(defn- fix-snaps-in [dir]
  (let [files (seq (.list (File. dir)))]
    (doseq [bad-eggs (filter identity (map #(re-seq bad-snapshot %) files))]
      (let [bad-egg (first bad-eggs)
            bad-file (File. (str dir "/" (first bad-egg)))
            good-filename (str (second bad-egg) "SNAPSHOT.jar")
            good-file (File. (str dir "/" good-filename))]
        (println "Renaming:" (.getAbsolutePath bad-file))
        (println "      To:" (.getAbsolutePath good-file))
        (.renameTo bad-file good-file)))))

(defn oh-snap [project]
  (fix-snaps-in (:library-path project))
  (fix-snaps-in (str (:library-path project) "/dev")))
