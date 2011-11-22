(ns joodo.controllers
  (:use
    [compojure.core :only (routing)])
  (:require
    [clojure.string :as str]))

(defn- namespaces-for-parts [root parts]
  (if (seq parts)
    (let [new-root (str root "." (first parts))
          controller-ns (str new-root "-controller")]
      (cons controller-ns (lazy-seq (namespaces-for-parts new-root (rest parts)))))
    []))

(defn namespaces-for-path [root path]
  (let [parts (filter #(not (empty? %)) (str/split path #"[/\.\?]"))]
    (namespaces-for-parts root parts)))

(defn resolve-controller
  "Loads any specific controllers that don't get loaded by the controller-router function"
  [ns-name]
  (try
    (let [controller-name (last (str/split (name ns-name) #"\."))
          ns-sym (symbol ns-name)]
      (require ns-sym)
      (if-let [controller-ns (find-ns ns-sym)]
        (ns-resolve controller-ns (symbol controller-name))))
    (catch Exception e nil)))

(defn- route-to-controller-ns [namespace cache request]
  (if-let [controller (resolve-controller namespace)]
    (do
      (dosync
        (alter cache assoc
          :handlers (conj (:handlers @cache) controller)
          namespace controller))
      (controller request))))

(defn- dynamic-controller-routing [root cache request]
  (loop [namespaces (namespaces-for-path (name root) (:uri request))]
    (if-let [namespace (first namespaces)]
      (if (contains? @cache namespace)
        (recur (rest namespaces))
        (or (route-to-controller-ns namespace cache request) (recur (rest namespaces)))))))

(defn- no-duplicates? [cache]
  (let [handlers (:handlers cache)]
    (= (count handlers) (count (set handlers)))))

(def controller-caches (ref {}))

(defn- controller-cache [root]
  (dosync
    (if-let [cache (get @controller-caches root)]
      cache
      (let [cache (ref {:handlers []} :validator no-duplicates?)]
        (alter controller-caches assoc root cache)
        cache))))

(defn clear-controller-caches []
  (dosync
    (doseq [cache (vals @controller-caches)]
      (ref-set cache {:handlers []}))))

(defn controller-router
  "Tells the application what namespaces to look for when loading controllers."
  [root]
  (let [cache (controller-cache root)]
    (fn [request]
      (if-let [response (apply routing request (:handlers @cache))]
        response
        (try
          (dynamic-controller-routing root cache request)
          (catch IllegalStateException e
            (apply routing request (:handlers @cache))))))))
