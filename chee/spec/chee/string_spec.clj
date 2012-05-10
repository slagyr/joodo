(ns chee.string-spec
  (:use
    [speclj.core]
    [chee.string]))

(describe "Chee String"

  (it "has gsub!"
    (should= "1.2.3" (gsub "1-2-3" #"\-" (fn [_] ".")))
    (should= "n.n.n" (gsub "1.2.3" #"\d" (fn [_] "n")))
    (should= "normal-name" (gsub "normalName" #"([a-z])([A-Z])" (fn [[_ lower upper]] (.toLowerCase (str lower "-" upper)))))
    (should= "321-654-987" (gsub "123-456-789" #"\d*" (fn [m] (apply str (reverse m)))))
    (should= "ABC XYZ" (gsub "CAB ZXY" #"(\w)(\w)(\w)" (fn [m] (str (nth m 2) (nth m 3) (nth m 1)))))
    (should= "1..2..3" (gsub "1.2.3" #"\." (fn [_] ".."))))

  (it "uses dashes in spear-case"
    (should= "normal-name" (spear-case "NormalName"))
    (should= "one" (spear-case "One"))
    (should= "one-two" (spear-case "OneTwo"))
    (should= "one-two-three" (spear-case "OneTwo-Three"))
    (should= "four" (spear-case "FOUR"))
    (should= "fou-r" (spear-case "FOuR"))
    (should= "fi-ve" (spear-case "FI_VE"))))

(run-specs)