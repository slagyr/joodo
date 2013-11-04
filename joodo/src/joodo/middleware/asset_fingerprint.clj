(ns joodo.middleware.asset-fingerprint
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [digest]))

(defn- add-checksum-to-filename [hash filename]
  (let [split-by-dot (string/split filename #"\.")]
    (if (< 1 (count split-by-dot))
      (string/join "."
        (conj (vec (butlast split-by-dot)) hash (last split-by-dot)))
      (str filename "." hash))))

(defn add-checksum-to-path [hash file-path]
  (let [split-string (string/split file-path #"/")]
    (string/join "/"
      (conj
        (vec (butlast split-string))
        (add-checksum-to-filename hash (last split-string))))))

(defn path-with-fingerprint [path]
  (let [resource (io/resource path)
        checksum (digest/md5 (io/input-stream resource))]
    (add-checksum-to-path checksum path)))

(defn path-without-fingerprint [path])

(defn resolve-fingerprint-in [request])

(defn wrap-asset-fingerprint [handler])