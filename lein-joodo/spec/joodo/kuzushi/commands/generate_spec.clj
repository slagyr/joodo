(ns joodo.kuzushi.commands.generate-spec
  (:use [speclj.core]
        [joodo.kuzushi.common :only (*project*)]
        [joodo.kuzushi.commands.generate :only (execute parse-args)]
        [joodo.kuzushi.generation :only (create-templater)])
  (:import [java.io File]
           [filecabinet FakeFileSystem Templater Templater$TemplaterLogger]))

(describe "Generate Command"

  (with fs (FakeFileSystem/installed))

  (it "parses generate command"
    (should (:*errors (parse-args)))
    (should= {:generator "foo" :ns "bar"} (parse-args "foo" "bar")))

  (it "should parse the force options"
    (should-not (:force (parse-args "name")))
    (should (:force (parse-args "-f" "name")))
    (should (:force (parse-args "--force" "name"))))

  (context "default"

    (with logger
      (proxy [Templater$TemplaterLogger] []
        (say [message])))
    (around [it]
      (with-redefs [create-templater
                    (fn [options]
                      (let [templater (Templater. "." "/templates")]
                        (.setLogger templater @logger)
                        templater))]
        (it)))

    (before
      (.createDirectory @fs "/home")
      (.setWorkingDirectory @fs "/home")
      (.createTextFile @fs "/templates/spec/app/controller_spec.clj" "controller_spec: !-CONTROLLER_NS-!:!-CONTROLLER_NAME-!:!-CONTROLLER_SUBJECT-!")
      (.createTextFile @fs "/templates/src/app/controller.clj" "controller: !-CONTROLLER_NS-!:!-CONTROLLER_NAME-!:!-CONTROLLER_SUBJECT-!")
      )

    (it "generates the controller files"
      (execute {:ns "test-project.controller.foo-controller" :generator "controller"})
      (should= "controller: test-project.controller.foo-controller:foo-controller:foo" (.readTextFile @fs "/home/src/test_project/controller/foo_controller.clj"))
      (should= "controller_spec: test-project.controller.foo-controller:foo-controller:foo" (.readTextFile @fs "/home/spec/test_project/controller/foo_controller_spec.clj")))

    (it "generates the controller files without -controller"
      (execute {:ns "test-project.controller.foo-controller" :generator "controller"})
      (should= "controller: test-project.controller.foo-controller:foo-controller:foo" (.readTextFile @fs "/home/src/test_project/controller/foo_controller.clj"))
      (should= "controller_spec: test-project.controller.foo-controller:foo-controller:foo" (.readTextFile @fs "/home/spec/test_project/controller/foo_controller_spec.clj")))

    (it "generates nested controller files"
      (execute {:ns "test-project.foo.bar" :generator "controller"})
      (should= "controller: test-project.foo.bar-controller:bar-controller:bar" (.readTextFile @fs "/home/src/test_project/foo/bar_controller.clj"))
      (should= "controller_spec: test-project.foo.bar-controller:bar-controller:bar" (.readTextFile @fs "/home/spec/test_project/foo/bar_controller_spec.clj")))
    )
  )

(run-specs)

