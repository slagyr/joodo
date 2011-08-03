(ns joodo.kake.servlet
  (:require
    [clojure.set :as set])
  (:use
    [ring.util.servlet :as rs :only (build-request-map merge-servlet-keys)]
    [ring.middleware.params :only (wrap-params)]
    [ring.middleware.keyword-params :only (wrap-keyword-params)]
    [ring.middleware.cookies :only (wrap-cookies)]
    [joodo.env :only (env development-env?)]
    [joodo.middleware.keyword-cookies :only (wrap-keyword-cookies)]
    [joodo.middleware.multipart-params :only (wrap-multipart-params)]
    [joodo.middleware.servlet-session :only (wrap-servlet-session)]
    [joodo.middleware.flash :only (wrap-flash)]
    [joodo.middleware.request :only (wrap-bind-request)])
  (:import
    [javax.servlet.http HttpServlet HttpServletRequest HttpServletResponse]
    [joodo.kake JoodoServlet]))

(defn update-servlet-response [^HttpServletResponse response, response-map]
  (when (not (and response (or (.isCommitted response) (:ignore-response response-map))))
    (rs/update-servlet-response response response-map)))

(defn make-service-method
  "Turns a handler into a function that takes the same arguments and has the
  same return value as the service method in the HttpServlet class."
  [handler]
  (fn [^HttpServlet servlet
       ^HttpServletRequest request
       ^HttpServletResponse response]
    (.setCharacterEncoding response "UTF-8")
    (let [request-map (-> request
      (build-request-map)
      (merge-servlet-keys servlet request response))]
      (if-let [response-map (handler request-map)]
        (update-servlet-response response response-map)
        (throw (NullPointerException. "Handler returned nil"))))))

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
  (let [handler (if (development-env?) (attempt-wrap handler 'joodo.middleware.verbose 'wrap-verbose) handler)]
    (->
      handler
      wrap-bind-request
      wrap-keyword-params
      wrap-params
      wrap-multipart-params
      wrap-flash
      wrap-keyword-cookies
      wrap-cookies
      wrap-servlet-session)))

(defn extract-joodo-handler []
  (let [core-namespace (env :joodo.core.namespace)
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

(extend-type JoodoServlet
  HandlerInstallable
  (install-handler [this handler]
    (.setServiceMethod this (make-service-method handler))))

(defn- read-src [src-path src-content]
  (let [rdr (-> (java.io.StringReader. src-content) (clojure.lang.LineNumberingPushbackReader.))
        file (java.io.File. src-path)
        parent-path (.getParent file)
        src-filename (.getName file)]
    (clojure.lang.Compiler/load rdr parent-path src-filename)))

(defn- load-config [ns path]
  (let [src (slurp path)]
    (binding [*ns* ns]
      (use 'clojure.core)
      (read-src path src))))

(defn load-configurations []
  (let [environment (System/getProperty "joodo-env")
        env-ns (create-ns (gensym (str "joodo.config-")))]
    (load-config env-ns "config/environment.clj")
    (load-config env-ns (format "config/%s.clj" environment))))

(defn initialize-joodo-servlet [servlet]
  (load-configurations)
  (let [handler (extract-joodo-handler)
        handler (if (development-env?) (attempt-wrap handler 'joodo.middleware.refresh 'wrap-refresh) handler)]
    (install-handler servlet handler)))

