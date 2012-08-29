(ns joodo.kuzushi.commands.new
  (:use
    [joodo.kuzushi.common :only (symbolize)]
    [joodo.kuzushi.version :only (joodo-version)]
    [joodo.kuzushi.generation :only (create-templater add-tokens ->path ->ns ->name)])
  (:import
    [filecabinet FileSystem]
    [mmargs Arguments]))

(def arg-spec (Arguments.))
(doto arg-spec
  (.addParameter "name" "The name of the new project.")
  (.addSwitchOption "f" "force" "Overwrite existing files"))

(defn parse-args [& args]
  (symbolize (.parse arg-spec (into-array String args))))

(defn- add-misc [options templater]
  (let [dir-name (:dir-name options)
        root-ns (:root-ns options)]
    (add-tokens templater "APP_NAME" root-ns "JOODO_VERSION" joodo-version)
    (.file templater (format "%s/project.clj" dir-name) "project.clj")
    (.file templater (format "%s/Procfile" dir-name) "Procfile")))

(defn- add-publics [options templater]
  (let [dir-name (:dir-name options)]
    (.binary templater (format "%s/public/images/joodo.png" dir-name) "public/images/joodo.png")
    (.file templater (format "%s/public/javascript/%s.js" dir-name dir-name) "public/javascript/default.js")
    (.file templater (format "%s/public/stylesheets/%s.css" dir-name dir-name) "public/stylesheets/default.css")))

(defn- add-default-src [options templater]
  (let [dir-name (:dir-name options)
        root-ns (:root-ns options)]
    (add-tokens templater "APP_NAME" root-ns)
    (add-tokens templater "DIR_NAME" dir-name)
    (.file templater (format "%s/spec/%s/root_spec.clj" dir-name dir-name) "spec/app/root_spec.clj")
    (.file templater (format "%s/src/%s/root.clj" dir-name dir-name) "src/app/root.clj")
    (.directory templater (format "%s/src/%s/controller" dir-name dir-name))
    (.file templater (format "%s/src/%s/view/view_helpers.clj" dir-name dir-name) "src/app/view/view_helpers.clj")
    (.file templater (format "%s/src/%s/view/layout.hiccup.clj" dir-name dir-name) "src/app/view/layout.hiccup.clj")
    (.file templater (format "%s/src/%s/view/index.hiccup.clj" dir-name dir-name) "src/app/view/index.hiccup.clj")
    (.file templater (format "%s/src/%s/view/not_found.hiccup.clj" dir-name dir-name) "src/app/view/not_found.hiccup.clj")))

(defn- add-configs [options templater]
  (let [dir-name (:dir-name options)
        root-ns (:root-ns options)]
    (add-tokens templater "APP_NAME" root-ns)
    (.file templater (format "%s/config/environment.clj" dir-name) "config/environment.clj")
    (add-tokens templater "ENV" "development")
    (.file templater (format "%s/config/development.clj" dir-name) "config/env.clj")
    (add-tokens templater "ENV" "production")
    (.file templater (format "%s/config/production.clj" dir-name) "config/env.clj")))

(defn execute
  "Creates all the needed files for new Joodo project."
  [options]
  (let [options (assoc options :dir-name (->path (:name options)))
        options (assoc options :root-ns (->ns (:name options)))
        templater (create-templater options)]
    (.createDirectory (FileSystem/instance) (:dir-name options))
    (add-misc options templater)
    (add-publics options templater)
    (add-default-src options templater)
    (add-configs options templater)))

