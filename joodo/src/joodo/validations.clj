(ns joodo.validations
  (:use
    [inflections.core]))

(defn validator [& validations]
  (fn [entity]
    (seq (filter identity (map #(% entity) validations)))))

(defn validate-presence-of
  ([field] (validate-presence-of field (str (name field) " is blank")))
  ([field message]
    (fn [record]
      (let [value (get record field)]
        (if (or (nil? value) (and (string? value) (empty? value)))
          [field message])))))

;(defmacro validate-uniqueness-of
;  ([field] `(validate-uniqueness-of ~field (str (name ~field) " is not unique")))
;  ([field message]
;    `(fn [~'record]
;      (if-let [value# (~field ~'record)]
;        (let [kind# (:kind ~'record)
;              key# (:key ~'record)
;              others# (joodo.datastore/find-by-kind kind# :filters [(~'= ~field value#)])]
;          (if (some #(not (= key# (:key %))) others#)
;            [~field ~message]))))))
