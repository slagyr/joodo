(ns ^{:doc "This namespace contains functions that are useful when dealing with strings."}
  chee.string
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

(def hump-pattern #"[a-z0-9][A-Z]")
(def non-camel-separator-pattern #"[_| |\-][A-Za-z]")
(def non-snake-separator-pattern #"[ |\-]")
(def non-spear-separator-pattern #"[ |\_]")

(defn separate-camel-humps [value]
  (gsub value hump-pattern
    #(str/join " " (seq %))))

(defn title-case
  "Converts the input string, which may be in any form, to a title-case string
  (title-case \"hello-world\") => \"Hello World\""
  [value]
  (str/join " "
    (map str/capitalize
      (str/split (separate-camel-humps value) #"[ |\-|_]"))))

(defn camel-case
  "Converts the input string, which may be in any form, to a camel-case string
  (camel-case \"hello-world\") => \"helloWorld\""
  [value]
  (gsub value non-camel-separator-pattern
    #(str/upper-case (apply str (rest %)))))

(defn capital-camel-case
  "Converts the input string, which may be in any form, to a capitalized camel-case string
  (capital-camel-case \"hello-world\") => \"HelloWorld\""
  [value]
  (let [camel (camel-case value)]
   (str (str/upper-case (.substring camel 0 1))
     (.substring camel 1 (.length camel)))))

(defn snake-case
  "Converts the input string, which may be in any form, to a snake-case string
  (snake-case \"hello-world\") => \"hello_world\""
  [value]
  (str/replace
    (str/lower-case (separate-camel-humps value))
    non-snake-separator-pattern
    "_"))

(defn spear-case
  "Converts the input string, which may be in any form, to a spear-case string.
  (spear-case \"Hello World\") => \"hello-world\""
  [value]
  (str/replace
    (str/lower-case (separate-camel-humps value))
    non-spear-separator-pattern
    "-"))

