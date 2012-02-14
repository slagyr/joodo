(ns joodo.kuzushi.generation-spec
  (:use
    [speclj.core]
    [joodo.kuzushi.generation])
  (:import
    [java.io File]
    [filecabinet FakeFileSystem]))

(describe "Generation"

  (with fs (FakeFileSystem/installed))

  (it "creates a templater"
    (let [templater (create-templater {:name "foo"})]
      (should= "." (.getDestinationRoot templater))
      (should= (str "file:" (.getCanonicalPath (File. "resources/joodo/kuzushi/templates")))
        (.getSourceRoot templater))))

  (it "creates a forceful templater"
    (let [templater (create-templater {:root "/foo/bar" :force "on"})]
      (should= true (.isForceful templater))))

  (it "converts to path"
    (should= "foo" (->path "foo"))
    (should= "foo/bar" (->path "foo.bar"))
    (should= "foo/bar" (->path "foo/bar"))
    (should= "foo/bar" (->path "foo\\bar"))
    (should= "foo_bar" (->path "foo-bar"))
    (should= "foo_bar" (->path "FOO-BAR")))

  (it "converts to var name"
    (should= "foo" (->name "foo"))
    (should= "foo" (->name "bar/foo"))
    (should= "foo" (->name "bar.foo"))
    (should= "foo" (->name "FOO"))
    (should= "foo-bar" (->name "foo_bar"))
    (should= "bang-bar" (->name "fizz/bang_bar")))


  (it "converts to ns"
    (should= "foo" (->ns "foo"))
    (should= "foo.bar" (->ns "foo.bar"))
    (should= "foo.bar" (->ns "foo/bar"))
    (should= "foo.bar" (->ns "foo\\bar"))
    (should= "foo-bar" (->ns "foo_bar"))
    (should= "foo" (->ns "FOO")))

  )