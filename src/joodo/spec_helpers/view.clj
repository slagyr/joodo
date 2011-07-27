(ns joodo.spec-helpers.view
  (:use
    [speclj.core :only (around)]
    [clojure.xml :only (parse)]
    [joodo.views :only (*view-context* render-template render-hiccup render-html)])
  (:import
    [speclj SpecFailure]))

(defn tag-matches? [node matcher]
  (not
    (some
      (fn [[key value]]
        (and
          (not (= value (get node key)))
          (not (= value (get (:attrs node) key)))))
      (seq matcher))))

(declare find-tag)

(defn- find-tag* [nodes matcher]
  (loop [nodes nodes]
    (if (not (seq nodes))
      nil
      (if-let [result (find-tag (first nodes) matcher)]
        result
        (recur (rest nodes))))))

(defn find-tag [node matcher]
  (if (tag-matches? node matcher)
    node
    (find-tag* (filter map? (:content node)) matcher)))

(defn rendered-content [content]
  (parse (java.io.ByteArrayInputStream. (.getBytes content))))

(defn rendered-html [template & kwargs]
  (rendered-content (apply render-html template kwargs)))

(defn rendered-hiccup [template & kwargs]
  (rendered-content (apply render-hiccup template kwargs)))

(defn rendered-template [template & kwargs]
  (rendered-content (apply render-template template kwargs)))

(defmacro should-have-tag [node matcher]
  `(if (not (find-tag ~node ~matcher))
    (throw (SpecFailure. (str "Failed to find tag: " ~matcher)))))

(defn with-view-context [& kwargs]
  (let [view-context (apply hash-map kwargs)]
    (around [it]
      (binding [*view-context* (merge *view-context* view-context)]
        (it)))))
