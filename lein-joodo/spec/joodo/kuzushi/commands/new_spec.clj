(ns joodo.kuzushi.commands.new-spec
  (:use
    [speclj.core]
    [joodo.kuzushi.commands.new :only (execute create-templater parse-args)])
  (:import
    [java.io File]
    [filecabinet FakeFileSystem Templater Templater$TemplaterLogger]))

(describe "New Command"

  (with fs (FakeFileSystem/installed))

  (it "creates a templater"
    (let [templater (create-templater {:name "foo"})]
      (should= "." (.getDestinationRoot templater))
      (should= (str "file:" (.getCanonicalPath (File. "resources/joodo/kuzushi/templates")))
        (.getSourceRoot templater))))

  (it "creates a forceful templater"
    (let [templater (create-templater {:root "/foo/bar" :force "on"})]
      (should= true (.isForceful templater))))

  (it "parses new command"
    (should (:*errors (parse-args)))
    (should= {:name "foo"} (parse-args "foo")))

  (it "should parse the force options"
    (should-not (:force (parse-args "name")))
    (should (:force (parse-args "-f" "name")))
    (should (:force (parse-args "--force" "name"))))

  (context "default"

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
      (.createTextFile @fs "/templates/src/app/core.clj" "core: !-APP_NAME-!")
      (.createTextFile @fs "/templates/src/app/view/view_helpers.clj" "view-helpers")
      (.createTextFile @fs "/templates/src/app/view/layout.hiccup.clj" "layout")
      (.createTextFile @fs "/templates/src/app/view/index.hiccup.clj" "index")
      (.createTextFile @fs "/templates/src/app/view/not_found.hiccup.clj" "not_found")
      (.createTextFile @fs "/templates/project.clj" "project: !-APP_NAME-!, joodo: !-JOODO_VERSION-!")
      (.createTextFile @fs "/templates/config/environment.clj" "default env, core: !-APP_NAME-!")
      (.createTextFile @fs "/templates/config/env.clj" "!-ENV-! env")
      )
    (before (execute {:name "app"}))

    (it "generates misc stuff"
      (should= (format "project: app, joodo: %s" "0.6.0-SNAPSHOT") (.readTextFile @fs "/home/app/project.clj")))

    (it "generated public dirs"
      (should= "joodo" (.readTextFile @fs "/home/app/public/images/joodo.png"))
      (should= "javascript" (.readTextFile @fs "/home/app/public/javascript/app.js"))
      (should= "css" (.readTextFile @fs "/home/app/public/stylesheets/app.css")))

    (it "generates default spec"
      (should= "core-spec: app" (.readTextFile @fs "/home/app/spec/app/core_spec.clj")))

    (it "generates default src"
      (should= "core: app" (.readTextFile @fs "/home/app/src/app/core.clj"))
      (should= "view-helpers" (.readTextFile @fs "/home/app/src/app/view/view_helpers.clj"))
      (should= "layout" (.readTextFile @fs "/home/app/src/app/view/layout.hiccup.clj"))
      (should= "index" (.readTextFile @fs "/home/app/src/app/view/index.hiccup.clj"))
      (should= "not_found" (.readTextFile @fs "/home/app/src/app/view/not_found.hiccup.clj"))
      (should= true (.exists @fs "/home/app/src/app/controller"))
      (should= true (.exists @fs "/home/app/src/app/model")))

    (it "generates config"
      (should= "default env, core: app" (.readTextFile @fs "/home/app/config/environment.clj"))
      (should= "development env" (.readTextFile @fs "/home/app/config/development.clj"))
      (should= "production env" (.readTextFile @fs "/home/app/config/production.clj")))
    )
  )

(run-specs)


