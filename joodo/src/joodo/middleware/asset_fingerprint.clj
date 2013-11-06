(ns joodo.middleware.asset-fingerprint
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [digest]))

(def fingerprinted-regex #"(.*)(\.fp[a-z0-9]{32})(\.?[^\./]*)?")
(def path-regex #"(.*?)(\.[^\./]*)?")

(defn add-fingerprint-to-path [hash file-path]
  (let [matches (re-matches path-regex file-path)]
    (str (second matches) ".fp" hash (last matches))))

(def path-with-fingerprint
  (memoize
    (fn
      ([path] (path-with-fingerprint path "public"))
      ([path prefix]
        (if-let [resource (io/resource (str prefix path))]
          (let [checksum (digest/md5 (io/input-stream resource))]
            (add-fingerprint-to-path checksum path))
          path)))))

(defn path-without-fingerprint [path]
  (if-let [match (re-matches fingerprinted-regex path)]
    (str (second match) (last match))
    path))

(defn resolve-fingerprint-in [request]
  (assoc request :uri (path-without-fingerprint (:uri request))))

(defn wrap-asset-fingerprint [handler]
  (fn [request]
    (let [request (resolve-fingerprint-in request)]
      (handler request))))
