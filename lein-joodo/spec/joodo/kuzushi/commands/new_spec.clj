(ns joodo.kuzushi.commands.new-spec
  (:use
    [speclj.core]
    [joodo.kuzushi.commands.new :only (execute parse-args)]
    [joodo.kuzushi.generation :only (create-templater)])
  (:require
    [joodo.kuzushi.version :as version])
  (:import
    [java.io File]
    [filecabinet FakeFileSystem Templater Templater$TemplaterLogger]))

(describe "New Command"

  (with fs (FakeFileSystem/installed))

  (it "parses new command"
    (should (:*errors (parse-args)))
    (should= {:name "foo"} (parse-args "foo")))

  (it "should parse the force options"
    (should-not (:force (parse-args "name")))
    (should (:force (parse-args "-f" "name")))
    (should (:force (parse-args "--force" "name"))))

  (context "with generation"

    (with logger
      (proxy [Templater$TemplaterLogger] []
        (say [message])))
    (around [it]
      (binding [create-templater
                (fn [options]
                  (let [templater (Templater. "." "/templates")]
                    (.setLogger templater @logger)
                    templater))]
        (it)))

    (before
      (.createDirectory @fs "/home")
      (.setWorkingDirectory @fs "/home")
      (.createTextFile @fs "/templates/public/images/joodo.png" "joodo")
      (.createTextFile @fs "/templates/public/javascript/default.js" "javascript")
      (.createTextFile @fs "/templates/public/stylesheets/default.css" "css")
      (.createTextFile @fs "/templates/spec/app/core_spec.clj" "core-spec: !-APP_NAME-!")
      (.createTextFile @fs "/templates/src/app/core.clj" "core: !-APP_NAME-!, dir: !-DIR_NAME-!")
      (.createTextFile @fs "/templates/src/app/view/view_helpers.clj" "view-helpers")
      (.createTextFile @fs "/templates/src/app/view/layout.hiccup.clj" "layout")
      (.createTextFile @fs "/templates/src/app/view/index.hiccup.clj" "index")
      (.createTextFile @fs "/templates/src/app/view/not_found.hiccup.clj" "not_found")
      (.createTextFile @fs "/templates/project.clj" "project: !-APP_NAME-!, joodo: !-JOODO_VERSION-!")
      (.createTextFile @fs "/templates/Procfile" "procfile")
      (.createTextFile @fs "/templates/config/environment.clj" "default env, core: !-APP_NAME-!")
      (.createTextFile @fs "/templates/config/env.clj" "!-ENV-! env")
      )
    (context "for 'app'"

      (before (execute {:name "app"}))

      (it "generates misc stuff"
        (should= (format "project: app, joodo: %s" version/string) (.readTextFile @fs "/home/app/project.clj"))
        (should= "procfile" (.readTextFile @fs "/home/app/Procfile")))

      (it "generated public dirs"
        (should= "joodo" (.readTextFile @fs "/home/app/public/images/joodo.png"))
        (should= "javascript" (.readTextFile @fs "/home/app/public/javascript/app.js"))
        (should= "css" (.readTextFile @fs "/home/app/public/stylesheets/app.css")))

      (it "generates default spec"
        (should= "core-spec: app" (.readTextFile @fs "/home/app/spec/app/core_spec.clj")))

      (it "generates default src"
        (should= "core: app, dir: app" (.readTextFile @fs "/home/app/src/app/core.clj"))
        (should= "view-helpers" (.readTextFile @fs "/home/app/src/app/view/view_helpers.clj"))
        (should= "layout" (.readTextFile @fs "/home/app/src/app/view/layout.hiccup.clj"))
        (should= "index" (.readTextFile @fs "/home/app/src/app/view/index.hiccup.clj"))
        (should= "not_found" (.readTextFile @fs "/home/app/src/app/view/not_found.hiccup.clj"))
        (should= true (.exists @fs "/home/app/src/app/controller")))

      (it "generates config"
        (should= "default env, core: app" (.readTextFile @fs "/home/app/config/environment.clj"))
        (should= "development env" (.readTextFile @fs "/home/app/config/development.clj"))
        (should= "production env" (.readTextFile @fs "/home/app/config/production.clj")))
      )

    (it "handles '-' in the app name"
      (execute {:name "foo-bar"})
      (should= (format "project: foo-bar, joodo: %s" version/string) (.readTextFile @fs "/home/foo_bar/project.clj"))
      (should= "joodo" (.readTextFile @fs "/home/foo_bar/public/images/joodo.png"))
      (should= "core-spec: foo-bar" (.readTextFile @fs "/home/foo_bar/spec/foo_bar/core_spec.clj"))
      (should= "core: foo-bar, dir: foo_bar" (.readTextFile @fs "/home/foo_bar/src/foo_bar/core.clj"))
      (should= "default env, core: foo-bar" (.readTextFile @fs "/home/foo_bar/config/environment.clj")))

    (it "handles '_' in the app name"
      (execute {:name "foo_bar"})
      (should= (format "project: foo-bar, joodo: %s" version/string) (.readTextFile @fs "/home/foo_bar/project.clj"))
      (should= "joodo" (.readTextFile @fs "/home/foo_bar/public/images/joodo.png"))
      (should= "core-spec: foo-bar" (.readTextFile @fs "/home/foo_bar/spec/foo_bar/core_spec.clj"))
      (should= "core: foo-bar, dir: foo_bar" (.readTextFile @fs "/home/foo_bar/src/foo_bar/core.clj"))
      (should= "default env, core: foo-bar" (.readTextFile @fs "/home/foo_bar/config/environment.clj")))
    )
  )

(run-specs)


