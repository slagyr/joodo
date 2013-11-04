(ns chee.util-spec
  (:require [speclj.core :refer :all]
            [chee.util :refer :all]))

(describe "util"

  (it "converts to options"
    (should= {} (->options nil))
    (should= {} (->options []))
    (should= {} (->options [{}]))
    (should= {:a 1} (->options [{:a 1}]))
    (should= {:a 1 :b 2} (->options [{:a 1} :b 2]))
    (should= {:a 1 :b 2} (->options [:a 1 :b 2]))))
