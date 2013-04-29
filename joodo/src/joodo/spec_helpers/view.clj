(ns ^{:doc "This namespace is comprised of functions that work with the Speclj testing framework to make testing view logic easy."}
  joodo.spec-helpers.view
  (:require
    [speclj.core :refer [around -fail]]
    [clojure.xml :refer [parse]]
    [chee.util :refer [->options]]
    [joodo.views :refer [*view-context* render-template render-hiccup render-html]]))

(defn tag-matches?
  "Expects two maps and returns true if the matcher map's keys
  and vals are in the node's map"
  [node matcher]
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

(defn find-tag
  "Finds and returns a tag within a tree of hiccup data. Expects the first
  argument to be hiccup data and the second arguement to be the tag in question."
  [node matcher]
  (if (tag-matches? node matcher)
    node
    (find-tag* (filter map? (:content node)) matcher)))

(defn rendered-content [content]
  (parse (java.io.ByteArrayInputStream. (.getBytes content))))

(defn rendered-html
  "Returns the html data provided within the default template. Expects the
  first argument to be the html data, and expects the additional arguments
  to be keys and values to be bound to *view-context*."
  [template & kwargs]
  (rendered-content (apply render-html template kwargs)))

(defn rendered-hiccup
  "Returns the hiccup data provided within the default template. Expects the
  first argument to be the hiccup data, and expects the additional arguments
  to be keys and values to be bound to *view-context*."
  [template & kwargs]
  (rendered-content (apply render-hiccup template kwargs)))

(defn rendered-template
  "Returns the hiccup data that lives in the specified template. Expects the
  first argument to be a valid location of a hiccup file and the optional
  additional arguments to be keys and values to be bound to *view-context*."
  [template & kwargs]
  (rendered-content (apply render-template template kwargs)))

(defmacro should-have-tag
  "Throws a SpecFailure if the specified tag isn't found in the node supplied
  by the arguments. Expects the first argument to be hiccup data and the second
  argument to be the tag in question."
  [node matcher]
  `(if (not (find-tag ~node ~matcher))
    (-fail (str "Failed to find tag: " ~matcher))))

(defn with-view-context
  "Allows your test to edit the *view-context* variable. Expects arguments
  in key value pairs.
  Ex. (with-view-context :template-root \"cleancoders/view\" :ns `project.views)"
  [& kwargs]
  (let [view-context (->options kwargs)]
    (around [it]
      (binding [*view-context* (merge *view-context* view-context)]
        (it)))))
