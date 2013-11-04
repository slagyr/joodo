(ns foo.main-spec
  (:require-macros [hiccups.core :as h]
                   [specljs.core :refer [around before context describe it should-contain should-not= should= with]])
  (:require [domina :as dom]
            [domina.css :as css]
            [domina.events :as event]
            [specljs.core]
            [foo.main :as main]))

(describe "Main"

  (it "foo"
    (should= :foo (main/foo)))

  )
