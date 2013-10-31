(ns leiningen.new.joodo
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files sanitize-ns year project-name]]))

(def render (renderer "joodo"))

(defn joodo
  "Generates Joodo App"
  [name]
  (let [data {:raw-name name
              :name (project-name name)
              :namespace (sanitize-ns name)
              :nested-dirs (name-to-path name)
              :year (year)}]
    (println "Generating a Joodo project called" name)
    (->files data
      ["project.clj" (render "project.clj" data)]
      ["README.md" (render "README.md" data)]
      ["bin/specljs" (render "bin/specljs")]
      ["config/environment.clj" (render "config/environment.clj" data)]
      ["config/development.clj" (render "config/development.clj" data)]
      ["public/stylesheets/{{name}}.css" (render "public/stylesheets/default.css" data)]
      ["src/clj/{{nested-dirs}}/main.clj" (render "src/clj/main.clj" data)]
      ["spec/clj/{{nested-dirs}}/main_spec.clj" (render "spec/clj/main_spec.clj" data)]
      ["src/cljs/{{nested-dirs}}/main.cljs" (render "src/cljs/main.cljs" data)]
      ["spec/cljs/{{nested-dirs}}/main_spec.cljs" (render "spec/cljs/main_spec.cljs" data)]
      ["src/clj/{{nested-dirs}}/view_helpers.clj" (render "src/clj/view_helpers.clj" data)]
      ["src/clj/{{nested-dirs}}/layout.hiccup" (render "src/clj/layout.hiccup" data)]
      ["src/clj/{{nested-dirs}}/index.hiccup" (render "src/clj/index.hiccup" data)]
      ["src/clj/{{nested-dirs}}/not_found.hiccup" (render "src/clj/not_found.hiccup" data)]
      )))
