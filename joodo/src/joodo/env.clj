(ns ^{:doc "This namespace holds functions that read the current environment that the application is in (according to the system properties)."}
  joodo.env)

(def ^{:doc "Holds information about the current environment. That data can be
  retrieved with the following syntax: (get @*env* :joodo-env)"}
  *env* (atom {:joodo-env (or (System/getProperty "joodo.env") "development")}))

(defn ^{:doc "Gets information about the current environment"} env [key]
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


