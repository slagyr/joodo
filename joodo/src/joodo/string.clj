(ns joodo.string)

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
