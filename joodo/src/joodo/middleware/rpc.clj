(ns joodo.middleware.rpc
  (:require [clojure.tools.reader.edn :as edn]
            [chee.util :refer [->options]]))

(def not-found {:status 404
                :headers {"Content-Type" "application/edn; charset=utf-8"}
                :body (pr-str "The requested service could not be found.")})

(defn- resolve-remote [remote prefix]
  (let [remote-sym (symbol remote)
        ns-sym (symbol (str prefix (namespace remote-sym)))
        var-sym (symbol (name remote-sym))]
    (try
      (when-not (contains? (loaded-libs) ns-sym)
        (require ns-sym))
      (let [var (ns-resolve (the-ns ns-sym) var-sym)]
        (when (:remote (meta var))
          var))
      (catch java.io.FileNotFoundException e nil))))

(defn serialize-response [response]
  (let [response-map (:response (meta response) {})]
    (pr-str response) ;; force any lazy STDOUT printing so it doesn't get into the response
    (merge
      {:status 202
       :headers {"Content-Type" "application/edn; charset=utf-8"}
       :body (pr-str response)}
      response-map)))

(defn base-rpc-handler [remote request params]
  (serialize-response (apply remote params)))

(defn process-remote [request prefix handler]
  (let [{{:keys [remote params]} :params} request
        parsed-params (edn/read-string params)]
    (if-let [remote-fn (resolve-remote remote prefix)]
      (handler remote-fn request parsed-params)
      not-found)))

(defn wrap-rpc
  "Middleware to process RPC calls.  "
  [handler & args]
  (let [options (->options args)
        prefix (name (:prefix options ""))
        middleware (:middleware options [])
        trigger-uri (:uri options "/_rpc")
        rpc-handler (reduce #(%2 %1) base-rpc-handler middleware)]
    (fn [{:keys [request-method uri] :as request}]
      (if (and (= :post request-method) (= trigger-uri uri))
        (process-remote request prefix rpc-handler)
        (handler request)))))

