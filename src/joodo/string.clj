(ns joodo.string)

(defn gsub [value pattern sub-fn]
  (loop [matcher (re-matcher pattern value) result [] last-end 0]
    (if (.find matcher)
      (recur matcher
          (conj result
            (.substring value last-end (.start matcher))
            (sub-fn (re-groups matcher)))
          (.end matcher))
      (apply str (conj result (.substring value last-end))))))
