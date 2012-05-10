(ns ^{:doc "This namespace contains functions that are used to display the view files."}
  joodo.views
  (:use [hiccup.core]
        [chee.util :only (->options)]))

(def ^{:doc "Var that holds a map with all the information required to render a page."}
  *view-context*
  {:template-root "view"
   :layout "layout"
   :ns `joodo.kake.default-rendering})

(defn- updated-context [kwargs]
  (merge *view-context* (->options kwargs)))

(defn- template-path [name ext]
  (format "%s/%s.%s" (:template-root *view-context*) name ext))

(defn- template-stream [name]
  (some
    identity
    (map
      #(.getResourceAsStream (clojure.lang.RT/baseLoader) (template-path name %))
      ["hiccup" "hiccup.clj"])))

(defn- read-template [name]
  (if-let [input (template-stream name)]
    (with-open [input input]
      (let [reader (java.io.PushbackReader. (java.io.InputStreamReader. input))]
        (loop [result ['list] object (read reader false :EOF)]
          (if (= :EOF object)
            (seq result)
            (recur (conj result object) (read reader false :EOF))))))
    (throw (Exception. (str "Template Not Found: " (template-path name "hiccup") "[.clj]")))))

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
    (let [layout-data (read-template layout)]
      (binding [*view-context* (assoc *view-context* :template-body body)]
        (render layout-data)))
    (render body)))

(defn- template-name [template]
  (if (keyword? template) (.substring (str template) 1) (name template)))

(defn render-hiccup
  "Expects hiccup data and any optional parameters. Transforms the hiccup data
  into html code and renders it into the default layout. If a layout is supplied
  in one of the optional parameters, it will render in the specified layout."
  [hiccup-src & kwargs]
  (binding [*view-context* (updated-context kwargs)]
    (render-in-layout hiccup-src)))

(def ^{:doc "Expects html or hiccup data and any optional parameters. Transforms
  any hiccup data provided and renders html into the default layout. If a layout is
  supplied in one of the optional parameters, it will render in the specified layout."}
  render-html render-hiccup)

(defn render-template
  "Expects the location of a template and any optional parameters. Returns the
  hiccup data located in the specified template. Also adds any parameters and
  their values to the *view-context*."
  [template & kwargs]
  (binding [*view-context* (updated-context kwargs)]
    (let [template-name (template-name template)
          template-src (read-template template-name)]
      (render-in-layout template-src))))

(defn render-partial
  "Expects the location of a partial and any optional parameters. Returns the
  hiccup data located in the specified partial. Also adds any parameters and
  their values to the *view-context*."
  [template & kwargs]
  (binding [*view-context* (updated-context kwargs)]
    (let [template (template-name template)
          parts (vec (.split (str template) "/"))
          parts (flatten (vector (pop parts) (str "_" (last parts))))
          template-name (apply str (interpose "/" parts))
          data (read-template template-name)]
      (eval data))))
