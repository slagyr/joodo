(ns ^{:doc "Contains middleware to print requests and responses."}
  joodo.middleware.verbose
  (:require [chee.pretty-map :refer [pretty-map]]))

(def request-count (atom 0))
(def endl (System/getProperty "line.separator"))

(defn wrap-verbose
  "Prints request and response maps to STDOUT in a human readable format."
  [handler]
  (fn [request]
    (let [request-id (swap! request-count inc)]
      (println
        (str "REQUEST " request-id " ========================================================================================"
          endl
          (pretty-map (dissoc request :servlet-request ))
          endl))
      (let [response (handler request)]
        (println
          (str "RESPONSE " request-id " ========================================================================================"
            endl
            (pretty-map (assoc response :body (str (count (str (:body response))) " chars of body")))
            endl))
        response))))
