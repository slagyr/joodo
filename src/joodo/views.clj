(ns joodo.views
  (:use
    [hiccup.core]))

(def *view-context* {
  :template-root "view"
  :layout "layout"
  :ns `joodo.kake.default-rendering
  })

(defn- updated-context [kwargs]
  (merge *view-context* (apply hash-map kwargs)))

(defn- template-path [name]
  (format "%s/%s.hiccup.clj" (:template-root *view-context*) name))

(defn- read-template [path]
  (if-let [input (.getResourceAsStream (clojure.lang.RT/baseLoader) path)]
    (with-open [input input]
      (let [reader (java.io.PushbackReader. (java.io.InputStreamReader. input))]
        (loop [result ['list] object (read reader false :EOF)]
          (if (= :EOF object)
            (seq result)
            (recur (conj result object) (read reader false :EOF))))))
    (throw (Exception. (str "Template Not Found: " path)))))

(defn- eval-content [content]
  (let [view-ns-sym (symbol (:ns *view-context*))]
    (require view-ns-sym)
    (binding [*ns* (the-ns view-ns-sym)]
      (eval content))))

(defn- render [content]
  (let [hiccup-src (eval-content content)]
    (html hiccup-src)))

(defn- render-in-layout [body]
  (if-let [layout (:layout *view-context*)]
    (let [layout-data (read-template (template-path layout))]
      (binding [*view-context* (assoc *view-context* :template-body body)]
        (render layout-data)))
    (render body)))

(defn- template-name [template]
  (if (keyword? template) (.substring (str template) 1) (name template)))

(defn render-hiccup [hiccup-src & kwargs]
  (binding [*view-context* (updated-context kwargs)]
    (render-in-layout hiccup-src)))

(def render-html render-hiccup)

(defn render-template [template & kwargs]
  (binding [*view-context* (updated-context kwargs)]
    (let [template-name (template-name template)
          template-path (template-path template-name)
          template-src (read-template template-path)]
      (render-in-layout template-src))))

(defn render-partial [template & kwargs]
  (binding [*view-context* (updated-context kwargs)]
    (let [template (template-name template)
          parts (.split (str template) "/")
          parts (reverse parts)
          parts (cons (str "_" (first parts)) (rest parts))
          parts (reverse parts)
          template-name (apply str (interpose "/" parts))
          template-path (template-path template-name)
          data (read-template template-path)]
      (eval data))))
