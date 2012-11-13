(ns joodo.handler
  (:require [clojure.set :as set]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.middleware.cookies :refer [wrap-cookies]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.flash :refer [wrap-flash]]
            [ring.middleware.file :refer [wrap-file]]
            [joodo.env :refer [env development-env? load-configurations]]
            [joodo.middleware.keyword-cookies :refer [wrap-keyword-cookies]]
            [joodo.middleware.servlet-session :refer [wrap-servlet-session]]
            [joodo.middleware.request :refer [wrap-bind-request]])
  (:import [javax.servlet.http HttpServlet HttpServletRequest HttpServletResponse]))

(def ^:dynamic *public-dir* "public")

(defn- attempt-to-load-var [ns-sym var-sym]
  (try
    (require ns-sym)
    (let [ns (the-ns ns-sym)]
      (ns-resolve ns var-sym))
    (catch Exception e
      (println "Failed to load var:" var-sym "from ns:" ns-sym e)
      nil)))

(defn- attempt-wrap [handler ns-sym var-sym]
  (if-let [wrapper (attempt-to-load-var ns-sym var-sym)]
    (wrapper handler)
    (do
      (println "Bypassing" var-sym)
      handler)))

(defn build-joodo-handler [handler]
  (println "(development-env?): " (development-env?))
  (let [handler (if (development-env?) (attempt-wrap handler 'joodo.middleware.verbose 'wrap-verbose) handler)]
    (->
      handler
      wrap-bind-request
      wrap-keyword-params
      wrap-params
      wrap-multipart-params
      wrap-flash
      wrap-keyword-cookies
      wrap-session)))

(defn extract-joodo-handler []
  (let [core-namespace (env :joodo.root.namespace )
        core-ns-sym (symbol core-namespace)
        _ (require core-ns-sym)
        core-ns (the-ns core-ns-sym)]
    (if-let [joodo-handler (ns-resolve core-ns 'joodo-handler)]
      joodo-handler
      (if-let [app-handler (ns-resolve core-ns 'app-handler)]
        (build-joodo-handler app-handler)
        (throw (Exception. (str core-namespace " must define app-handler or joodo-handler")))))))

(defprotocol HandlerInstallable
  (install-handler [_ handler]))

(defn load-handler []
  (let [handler (extract-joodo-handler)
        handler (if (development-env?) (attempt-wrap handler 'joodo.middleware.refresh 'wrap-refresh) handler)]
    (wrap-file handler *public-dir*)))

