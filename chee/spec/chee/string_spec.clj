(ns chee.string-spec
  (:require [speclj.core :refer :all]
            [chee.string :refer :all]))

(describe "Chee String"

  (it "has gsub!"
    (should= "1.2.3" (gsub "1-2-3" #"\-" (fn [_] ".")))
    (should= "n.n.n" (gsub "1.2.3" #"\d" (fn [_] "n")))
    (should= "normal-name" (gsub "normalName" #"([a-z])([A-Z])" (fn [[_ lower upper]] (.toLowerCase (str lower "-" upper)))))
    (should= "321-654-987" (gsub "123-456-789" #"\d*" (fn [m] (apply str (reverse m)))))
    (should= "ABC XYZ" (gsub "CAB ZXY" #"(\w)(\w)(\w)" (fn [m] (str (nth m 2) (nth m 3) (nth m 1)))))
    (should= "1..2..3" (gsub "1.2.3" #"\." (fn [_] ".."))))

  (it "splits camel humps"
    (should= "Ab Cd" (separate-camel-humps "AbCd"))
    (should= "Abc0 Xyz1" (separate-camel-humps "Abc0Xyz1")))

  (it "converts strings into titles"
    (should= "Class Name" (title-case "Class Name"))
    (should= "Class Name" (title-case "class_name"))
    (should= "Once Upon A Time" (title-case "once_upon_a_time"))
    (should= "Once Upon A Time" (title-case "once-upon-a-time"))
    (should= "Ab C E Fg Hi J" (title-case "AbC-eFg-hiJ"))
    (should= "Ab C E Fg Hi J" (title-case "AbC_eFg_hiJ"))
    (should= "With Spaces" (title-case "with spaces"))
    (should= "Some Title" (title-case "Some Title"))
    (should= "Some Title" (title-case "SomeTitle")))

  (it "can camalize strings"
    (should= "defaultSceneName" (camel-case "defaultSceneName"))
    (should= "setDefaultSceneName" (camel-case "set defaultSceneName"))
    (should= "className" (camel-case "class_name"))
    (should= "onceUponATime" (camel-case "once_upon_a_time"))
    (should= "withSpaces" (camel-case "with spaces"))
    (should= "withDash" (camel-case "with-dash"))
    (should= "startingCapital" (camel-case "starting Capital")))

  (it "can do capital camel case too"
    (should= "DefaultSceneName" (capital-camel-case "DefaultSceneName"))
    (should= "DefaultSceneName" (capital-camel-case "defaultSceneName"))
    (should= "ClassName" (capital-camel-case "class_name"))
    (should= "OnceUponATime" (capital-camel-case "once_upon_a_time"))
    (should= "WithSpaces" (capital-camel-case "with spaces"))
    (should= "WithDash" (capital-camel-case "with-dash")))

  (it "can snake-case strings"
    (should= "class_name" (snake-case "class_name"))
    (should= "class_name" (snake-case "ClassName"))
    (should= "one_two_three" (snake-case "OneTwoThree"))
    (should= "one" (snake-case "One"))
    (should= "one_two_three" (snake-case "one-two-three"))
    (should= "one_two_three" (snake-case "one two three")))

  (it "can spear-case strings"
    (should= "normal-name" (spear-case "NormalName"))
    (should= "one" (spear-case "One"))
    (should= "one-two" (spear-case "OneTwo"))
    (should= "one-two-three" (spear-case "OneTwo-Three"))
    (should= "one-two-three" (spear-case "one_two-Three"))
    (should= "four" (spear-case "FOUR"))
    (should= "fou-r" (spear-case "FOuR"))
    (should= "fi-ve" (spear-case "FI_VE")))

  )

(run-specs)
