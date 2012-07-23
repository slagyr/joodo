(ns ^{:doc "This namespace contains middleware that digests locale
  information and adds it to the request map."}
  joodo.middleware.locale
  (:use [clojure.string :as string :only [split join]]))

(defn build-query-map [query-string]
  (apply hash-map (string/split query-string #"=|&")))

(defn split-locale-and-q [locale-and-q-score]
  (string/split locale-and-q-score #";"))

(defn add-implicit-q-score [locale-q-pair]
  (if (= 1 (count locale-q-pair))
    (conj locale-q-pair "q=1")
    locale-q-pair))

(defn locale-in-accept-header [accepted-locales accept-header]
  (if (not (empty? accept-header))
    (let [locale-and-q-scores-list (string/split accept-header #",")
          locale-and-q-pairs (map add-implicit-q-score (map split-locale-and-q locale-and-q-scores-list))
          accepted-locale-and-q-pairs (filter #(contains? accepted-locales (first %)) locale-and-q-pairs)
          sorted-locale-and-q-pairs (sort #(compare (last %2) (last %1)) accepted-locale-and-q-pairs)]
      (ffirst sorted-locale-and-q-pairs))))

(defn locale-in-query-string [accepted-locales query-string]
  (if (not (empty? query-string))
    (let [query-map (build-query-map query-string)
          possible-locale (get query-map "locale")]
      (if (contains? accepted-locales possible-locale)
        possible-locale))))

(defn locale-in-uri [accepted-locales uri]
  (let [possible-locale (second (string/split uri #"\/"))]
    (if (contains? accepted-locales possible-locale)
      possible-locale)))

(defn scrub-locale-from-uri [request]
  (update-in
    request
    [:uri]
    #(str "/" (join (interpose "/" (rest (rest (string/split % #"\/"))))))))

(defn add-locale [request locale]
  (assoc request :locale locale))

(defn wrap-locale
  "Grabs locale and puts it into the request map if the locale is a
  supported locale in the following order:
  1. URI - Sets locale based on the first part of the uri and scrubs
      the uri of any locale information.
  2. Query Params - Pulls locale from query params.
  3. HTTP Header - Checks the accept-language field in the http header."
  [handler accepted-locales]
  (fn [request]
    (if-let [locale (locale-in-uri accepted-locales (:uri request))]
      (handler (scrub-locale-from-uri (add-locale request locale)))
      (if-let [locale (locale-in-query-string accepted-locales (:query-string request))]
        (handler (add-locale request locale))
        (if-let [locale (locale-in-accept-header accepted-locales (get (:headers request) "accept-language"))]
          (handler (add-locale request locale))
          (handler (add-locale request "")))))))
