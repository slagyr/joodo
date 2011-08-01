(ns joodo.kuzushi.commands.new
  (:use
    [joodo.kuzushi.common :only (symbolize)]
    [joodo.kuzushi.version :only (joodo-version)])
  (:import
    [filecabinet FileSystem Templater]
    [mmargs Arguments]))

(def arg-spec (Arguments.))
(doto arg-spec
  (.addParameter "name" "The name of the new project.")
  (.addSwitchOption "f" "force" "Overwrite existing files"))

(defn parse-args [& args]
  (symbolize (.parse arg-spec (into-array String args))))

(defn create-templater [options]
  (let [destination "."
        templates-marker (.toString (.getResource (clojure.lang.RT/baseLoader) "joodo/kuzushi/templates/marker.txt"))
        source (.parentPath (FileSystem/instance) templates-marker)
        templater (Templater. destination source)]
    (when (:force options)
      (.setForceful templater true))
    templater))

(defn add-tokens [templater & kvargs]
  (let [tokens (apply hash-map kvargs)]
    (doseq [[token value] tokens]
      (.addToken templater token value))))

(defn- add-misc [options templater]
  (let [name (:name options)]
    (add-tokens templater "APP_NAME" name "JOODO_VERSION" joodo-version)
    (.file templater (format "%s/project.clj" name) "project.clj")))

(defn- add-publics [options templater]
  (let [name (:name options)]
    (.binary templater (format "%s/public/images/joodo.png" name) "public/images/joodo.png")
    (.file templater (format "%s/public/javascript/%s.js" name name) "public/javascript/default.js")
    (.file templater (format "%s/public/stylesheets/%s.css" name name) "public/stylesheets/default.css")))

(defn- add-default-src [options templater]
  (let [name (:name options)]
    (add-tokens templater "APP_NAME" name)
    (.file templater (format "%s/spec/%s/core_spec.clj" name name) "spec/app/core_spec.clj")
    (.file templater (format "%s/src/%s/core.clj" name name) "src/app/core.clj")
    (.directory templater (format "%s/src/%s/controller" name name))
    (.directory templater (format "%s/src/%s/model" name name))
    (.file templater (format "%s/src/%s/view/view_helpers.clj" name name) "src/app/view/view_helpers.clj")
    (.file templater (format "%s/src/%s/view/layout.hiccup.clj" name name) "src/app/view/layout.hiccup.clj")
    (.file templater (format "%s/src/%s/view/index.hiccup.clj" name name) "src/app/view/index.hiccup.clj")
    (.file templater (format "%s/src/%s/view/not_found.hiccup.clj" name name) "src/app/view/not_found.hiccup.clj")))

(defn execute
  "Creates all the needed files for new Joodo project."
  [options]
  (let [options (assoc options :name (.toLowerCase (:name options)))
        templater (create-templater options)]
    (.createDirectory (FileSystem/instance) (:name options))
    (add-misc options templater)
    (add-publics options templater)
    (add-default-src options templater)))

