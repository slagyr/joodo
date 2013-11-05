(ns chee.pretty-map-spec
  (:require [speclj.core :refer :all]
            [chee.pretty-map :refer :all]))



  (def full-output "{
    :a  1
    :b  2
    :c  3}")

  (def nested-output "{
    :a   1
    :a_  {
        :x  24
        :y  25
        :z  26}
    :b   2}")

  (def sorted-output "{
    a       midnight
    dreary  while
    i       pondered
    once    upon
    weak    and
    weary   there}")

(describe "Pretty Map"

  (it "makes an empty map pretty"
    (should= "{}" (pretty-map {})))

  (it "makes a simple map pretty"
    (should= "{:a 1}" (pretty-map {:a 1})))

  (it "makes a full map pretty"
    (should= full-output (pretty-map {:a 1 :b 2 :c 3})))

  (it "makes a nested map pretty"
    (should= nested-output (pretty-map {:a 1 :a_ {:x 24 :y 25 :z 26} :b 2})))

  (it "makes the map sorted"
    (should= sorted-output (pretty-map (apply hash-map (.split "once upon a midnight dreary while i pondered weak and weary there" " "))))))

(run-specs)
