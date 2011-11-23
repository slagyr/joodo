(ns ^{:doc "This namespace contains functions that output the current state of the server."}
  joodo.middleware.verbose
  (:use
    [joodo.util.pretty-map :only (pretty-map)]))

(def ^{:doc ""}
  request-count (atom 0))

(defn wrap-verbose
  "Outputs the state of the server to standard output as requests are getting made."
  [handler]
  (fn [request]
    (let [request-id (swap! request-count inc)]
      (println "REQUEST " request-id " ========================================================================================")
      (print (pretty-map (dissoc request :servlet-request)))
      (println)
      (println)
      (let [response (handler request)]
        (println "RESPONSE " request-id " ========================================================================================")
        (print (pretty-map (assoc response :body (str (count (str (:body response))) " chars of body"))))
        (println)
        (println)
        response))))
