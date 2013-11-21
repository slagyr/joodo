(ns joodo.middleware.asset-fingerprint
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [digest]
            [joodo.env :as env]))

(def fingerprinted-regex #"(.*)(\.fp[a-z0-9]{32})(\.?[^\./]*)?")
(def path-regex #"(.*?)(\.[^\./]*)?")

(defn add-fingerprint-to-path [hash file-path]
  (let [matches (re-matches path-regex file-path)]
    (str (second matches) ".fp" hash (last matches))))

(def add-fingerprint
  "Returns a path with the resources fingerprint (checksum) in the filename.
   Using fingerprinted paths prevents the browser from using previously cached versions
   of your assets.

     (add-fingerprint \"/stylesheets/my.css\") ;=> /stylesheets/my.fp084994104d0fdf138f53f0e4d94a5486.css

   The path must exist in the classpath.

   The prefix param defaults to \"public\".  This is added to the beginning of the path when
   loaded form the class path.  e.g. If the asset /stylesheets/my.css is being loaded, it likely
   exists inside /public/stylesheets/my.css."
  (memoize
    (fn
      ([path] (add-fingerprint path "public"))
      ([path prefix]
        (if (env/development?)
          path
          (if-let [resource (io/resource (str prefix path))]
            (let [checksum (digest/md5 (io/input-stream resource))]
              (add-fingerprint-to-path checksum path))
            path))))))

(defn remove-fingerprint [path]
  (if-let [match (re-matches fingerprinted-regex path)]
    (str (second match) (last match))
    path))

(defn resolve-fingerprint-in [request]
  (assoc request :uri (remove-fingerprint (:uri request))))

(defn wrap-asset-fingerprint
  "Middleware the looks for fingerprinted asset requests and removes the fingerprints.

  e.g. requests for /stylesheets/my.fp084994104d0fdf138f53f0e4d94a5486.css turn into
  requests for /stylesheets/my.css"
  [handler]
  (fn [request]
    (let [request (resolve-fingerprint-in request)]
      (handler request))))
