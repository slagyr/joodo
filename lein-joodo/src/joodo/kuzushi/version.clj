(ns joodo.kuzushi.version
  (:require
    [clojure.string :as str]))

(def major 0)
(def minor 10)
(def tiny 0)
(def snapshot false)
(def string
  (str
    (str/join "." (filter identity [major minor tiny]))
    (if snapshot "-SNAPSHOT" "")))
(def summary (str "joodo/lein-joodo " string))

(def joodo-version "0.10.0")
