(ns ^{:doc "This namespace contains functions that are useful when dealing with strings."}
  joodo.string
  (:require
    [clojure.string :as str]))

(defn gsub
  "Matches patterns and replaces those matches with a specified value.
  Expects a string to run the operation on, a pattern in the form of a
  regular expression, and a function that handles the replacing."
  [value pattern sub-fn]
  (loop [matcher (re-matcher pattern value) result [] last-end 0]
    (if (.find matcher)
      (recur matcher
          (conj result
            (.substring value last-end (.start matcher))
            (sub-fn (re-groups matcher)))
          (.end matcher))
      (apply str (conj result (.substring value last-end))))))

(defn spear-case [value]
  (str/lower-case
    (gsub
      (str/replace (name value) "_" "-")
      #"([a-z])([A-Z])" (fn [[_ lower upper]] (str lower "-" upper)))))
