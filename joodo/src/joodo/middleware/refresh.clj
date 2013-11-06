(ns ^{:doc "This namespace contains functions that interface with the fresh library. The fresh library automatically loads up files that have recently been changed."}
  joodo.middleware.refresh
  (:require [clojure.string :as string]
            [fresh.core :refer [freshener ns-to-file]]
            [joodo.env :as env]
            [taoensso.timbre :as timbre]))

(def endl (System/getProperty "line.separator"))

(def cache (ref {}))

(defn- handler-symbols [handler-sym]
  (if-let [namespace-str (namespace handler-sym)]
    [(symbol namespace-str) (symbol (name handler-sym))]
    [(symbol (name handler-sym)) 'handler]))

(defn resolve-handler [ns-sym var-sym]
  (require ns-sym)
  (if-let [handler-var (ns-resolve (the-ns ns-sym) var-sym)]
    @handler-var
    (throw (Exception. (str "No such var " (name ns-sym) "/" (name var-sym))))))

(defn resolve-cached-handler [ns-file ns-sym var-sym]
  (dosync
    (if-let [record (get-in @cache [ns-file var-sym])]
      (:handler record)
      (let [handler (resolve-handler ns-sym var-sym)]
        (alter cache assoc-in [ns-file var-sym] {:handler handler :ns ns-sym :var var-sym})
        handler))))

(defn handler
  "In development, returns a dynamic handler that reloads the specified handler when changed.
  In non-development, loads the handler. No fluff."
  [handler-sym]
  (let [[ns-sym var-sym] (handler-symbols handler-sym)]
    (if (env/development?)
      (let [ns-file (ns-to-file (name ns-sym))]
        (fn [request]
          (if-let [handler (resolve-cached-handler ns-file ns-sym var-sym)]
            (handler request)
            (throw (Exception. (str "Missing chached handler " (name ns-sym) "/" (name var-sym)))))))
      (resolve-handler ns-sym var-sym))))

(defn- files-to-keep-fresh []
  (let [ns-es (all-ns)
        ns-es (filter #(not (.startsWith (.toString (.name %)) "joodo")) ns-es)]
    (filter identity (map #(ns-to-file (.name %)) ns-es))))

(defn audit-refresh [report]
  (when-let [reloaded (seq (:reloaded report))]
    (timbre/info (str "Reloading..." endl "\t"
        (string/join (str endl "\t") (map #(.getAbsolutePath %) reloaded))))
    (when-let [cached-entries (seq (filter #(contains? @cache %) reloaded))]
      (let [records (mapcat #(vals (get @cache %)) cached-entries)]
        (timbre/info (str "Clearing handlers from cache..." endl "\t"
            (string/join (str endl "\t") (map #(str (:ns %) "/" (:var %)) records)))))
      (dosync
        (doseq [file cached-entries]
          (alter cache dissoc file)))))
  true)

(defn wrap-refresh
  "When integrated with the current ring handler, this function reloads newly saved files (such as controllers) while in development mode."
  [handler]
  (let [refresh! (freshener files-to-keep-fresh audit-refresh)]
    (fn [request]
      (refresh!)
      (handler request))))
