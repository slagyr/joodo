(ns ^{:doc "This namespace contains functions that creates a ring-handler and dynamically loads controllers"}
  joodo.controllers
  (:require [clojure.string :as str]
            [compojure.core :refer [routing]]))

(defn- namespaces-for-parts [root parts]
  (if (seq parts)
    (let [new-root (str root "." (first parts))]
      (cons
        (str new-root "-controller")
        (cons
          (str new-root "." (first parts) "-controller")
          (lazy-seq (namespaces-for-parts new-root (rest parts))))))
    []))

(defn namespaces-for-path [root path]
  (let [parts (filter #(not (empty? %)) (str/split path #"[/\.\?]"))]
    (namespaces-for-parts root parts)))

(defn resolve-controller
  "An attempt will be made to load the specified namespace and extract the controller var
  defined within.  nil is returned upon failure."
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
  "Creates a dynamic handler to load controllers based on the url. This feature allows Joodo apps to
  startup quickly while the remainder of the system loads on an as-needed basis.
  Given the handler declaration
    (controller-rounter 'acme)
  and the URL
    http://host/widget/action
  the handler will attempt to load (require) the namespace 'acme.widget-controller or 'acme.widget.widget-controller
  if the other doesn't exist. If either namespace is successfully loaded the #'widget-controller var, which must
  be a ring handler defined within namespace, will handle the request.
  Controller may be nested.  For example, the URL http://host/widget/doodad/action will cause the handler to look
  for namespaces 'acme.widget.doodad-controller or 'acme.widget.doodad.doodad-controller.
  Requests with no matching controller pass through these handlers with nil response."
  [root]
  (let [cache (controller-cache root)]
    (fn [request]
      (if-let [response (apply routing request (:handlers @cache))]
        response
        (try
          (dynamic-controller-routing root cache request)
          (catch IllegalStateException e
            (apply routing request (:handlers @cache))))))))
