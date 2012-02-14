(ns joodo.kuzushi.generation
  (:use
    [joodo.kuzushi.common :only (*lib-name*)])
  (:import
    [filecabinet FileSystem Templater]))

(defn create-templater [options]
  (let [destination "."
        templates-marker (.toString (.getResource (clojure.lang.RT/baseLoader) (str *lib-name* "/kuzushi/templates/marker.txt")))
        source (.parentPath (FileSystem/instance) templates-marker)
        templater (Templater. destination source)]
    (when (:force options)
      (.setForceful templater true))
    templater))

(defn add-tokens [templater & kvargs]
  (let [tokens (apply hash-map kvargs)]
    (doseq [[token value] tokens]
      (.addToken templater token value))))

(defn ->path [name]
  (.replaceAll
    (.replaceAll
      (.replaceAll
        (.toLowerCase name)
        "-", "_")
      "\\." "/")
    "\\\\", "/"))

(defn ->name [path]
  (.replaceAll
    (last (seq (.split (->path path) "[/.]")))
    "_", "-"))

(defn ->ns [name]
  (.replaceAll
    (.replaceAll
      (.replaceAll
        (.toLowerCase name)
        "\\\\" "/")
      "/" ".")
    "_" "-"))