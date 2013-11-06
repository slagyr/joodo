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

(defn remove-checksum-from-filename [filename]
  (let [split-string (string/split filename #"\.")]
    (cond (< 2 (count split-string))
          (string/join "."
            (conj (vec (butlast (butlast split-string)))
              (last split-string)))
          (< 1 (count split-string))
          (apply str (butlast split-string)))))

(defn remove-checksum-from-path [path]
  (let [split-string (string/split path #"/")]
    (string/join "/"
      (conj
        (vec (butlast split-string))
        (remove-checksum-from-filename (last split-string))))))

(defn path-without-fingerprint [path]
  (remove-checksum-from-path path))

(defn path-has-fingerprint [path]
  (let [split-by-slash (string/split path #"/")
        split-string (string/split (last split-by-slash) #"\.")]
          (not (nil? (some #(= 32 (count  %)) split-string)))))

(defn resolve-fingerprint-in [request]
  (if (path-has-fingerprint (:uri request))
    (assoc request :uri (remove-checksum-from-path (:uri request)))
    request))

(defn wrap-asset-fingerprint [handler]
  (fn [request]
    (let [request (resolve-fingerprint-in request)]
      (handler request))))
