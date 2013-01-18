(ns ^{:doc "This namespace contains middleware that digests locale
  information and adds it to the request map."}
  joodo.middleware.locale
  (:use
    [clojure.string :as string :only [split join]]))


(defn- scrubbed-uri [uri]
  (str "/" (string/join "/" (nnext (string/split uri #"\/")))))

(defn- acceptable-locale? [possible-locale accepted-locales]
  (or (contains? accepted-locales possible-locale) (empty? accepted-locales)))

(defn- has-accepted-locale? [request options]
  (if-let [locale (:locale request)]
    (acceptable-locale? locale (:accepted-locales options))
    false))

(defn- split-locale-and-q [locale-and-q-score]
  (string/split locale-and-q-score #";"))

(defn- add-implicit-q-score [locale-q-pair]
  (if (= 1 (count locale-q-pair))
    (conj locale-q-pair "q=1")
    locale-q-pair))

(defn- accepted-locale-and-q-pairs [accept-header options]
  (let [locale-and-q-scores-list (string/split accept-header #",")
        locale-and-q-pairs (map add-implicit-q-score (map split-locale-and-q locale-and-q-scores-list))]
    (filter #(acceptable-locale? (first %) (:accepted-locales options)) locale-and-q-pairs)))


(defn uri
  "Adds the locale from the uri. It assumes the first section of the uri is the locale."
  [request options]
  (let [locale (second (string/split (:uri request) #"\/"))]
    (assoc request :locale locale :uri (scrubbed-uri (:uri request)))))

(defn cookie
  "Adds the locale from the cookies. The default cookie it uses is called 'locale', but you can specify the
  locale-cookie-name option to alter that."
  [request options]
  (let [locale-cookie-name (or (:locale-cookie-name options) "locale")]
    (assoc request :locale (:value (get (:cookies request) locale-cookie-name)))))

(defn query-param
  "Adds the locale from the query params. The default param it uses is called 'locale', but you can specify the
  locale-param-name option to alter that."
  [request options]
  (let [locale-param-name (or (:locale-param-name options) "locale")]
    (assoc request :locale (get (:query-params request) locale-param-name))))

(defn accept-header
  "Adds the locale with the highest q rating from the accept-header. It only uses locales listed in the accepted locales set."
  [request options]
  (let [accept-header (or (get (:headers request) "accept-language") "")
        sorted-locale-and-q-pairs (sort #(compare (last %2) (last %1)) (accepted-locale-and-q-pairs accept-header options))
        locale (ffirst sorted-locale-and-q-pairs)]
    (assoc request :locale (if (not (= "" locale)) locale))))


(defn wrap-locale
  "Adds locale information into your request map. You must supply a list of locale-augmenter functions
  in the order you want them. This namespace provides you with 4 locale augmenters (accept-header, query-param,
  cookie, and uri). You may pass in any custom locale augmenting functions you wish. The expectation is that
  each locale augmenter will accept a request map and some wrap-locale's options and return a request map with
  locale data in it. You may also specify a unique set of accepted locales and a default locale."
  [handler & options]
  (let [options (apply hash-map options)]
    (fn [request]
      (let [augmented-requests (map #(% request options) (:locale-augmenters options))
            requests-with-accepted-locale-data (filter #(has-accepted-locale? % options) augmented-requests)]
        (handler (or
          (first requests-with-accepted-locale-data)
          (assoc request :locale (:default-locale options))))))))
