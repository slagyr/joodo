(ns ^{:doc "This namespace contains functions that interface with the fresh library. The fresh library automatically loads up files that have recently been changed."}
  joodo.middleware.refresh
  (:require [fresh.core :refer [freshener ns-to-file]]
            [joodo.env :as env]))

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

(defn handler [handler-sym]
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
    (println "Reloading...")
    (doseq [file reloaded] (println (.getAbsolutePath file)))
    (println "")
    (when-let [cached-entries (seq (filter #(contains? @cache %) reloaded))]
      (println "Clearing handlers from cache...")
      (let [records (mapcat #(vals (get @cache %)) cached-entries)]
        (doseq [record records]
          (println (str (:ns record) "/" (:var record)))))
      (dosync
        (doseq [file cached-entries]
          (alter cache dissoc file)))
      ))
  true)

(defn- clear-controller-caches []
  (try
    ; To avoid dependency on kake, we dynamically invoke the clear-controller-caches fn.
    (require 'joodo.controllers)
    (let [controllers-ns (find-ns 'joodo.controllers)
          clear-fn (ns-resolve controllers-ns 'clear-controller-caches)]
      (clear-fn))
    (catch Exception e
      (println "ON NO!!!  Can't clear controller cache." e))))

(defn wrap-refresh
  "When integrated with the current ring handler, this function reloads newly saved files (such as controllers) while in development mode."
  [handler]
  (let [refresh! (freshener files-to-keep-fresh audit-refresh)]
    (fn [request]
      (refresh!)
      (clear-controller-caches)
      (handler request))))
