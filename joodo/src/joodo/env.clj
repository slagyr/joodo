(ns joodo.env)

(def *env* (atom {:joodo-env (or (System/getProperty "joodo.env") "development")}))

(defn env [key]
  (or
    (get @*env* (keyword key))
    (get @*env* (str key))
    (System/getProperty (str key))))

(defn development-env? []
  (= "development" (env :joodo-env)))

(defn production-env? []
  (= "production" (env :joodo-env)))


