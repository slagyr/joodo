(ns joodo.env)

(def *env* (atom {:joodo-env (or (System/getProperty "joodo.env") "development")}))

(defn env [key]
  (or
    (get @*env* (keyword key))
    (get @*env* (str key))
    (System/getProperty (str key))))

(defn development-env?
  "Returns true if the application is running in the
  development environment and false otherwise"
  []
  (= "development" (env :joodo-env)))

(defn production-env?
  "Returns true if the application is running in the
  production environment and false otherwise"
  []
  (= "production" (env :joodo-env)))


