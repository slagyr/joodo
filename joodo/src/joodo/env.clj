(ns joodo.env)

(def env (or (System/getProperty "joodo.env") "development"))

(defn development-env? []
  (= "development" env))

(defn production-env? []
  (= "production" env))
