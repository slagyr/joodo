(ns joodo.middleware.verbose
  (:use
    [joodo.util.pretty-map :only (pretty-map)]))

(def request-count (atom 0))

(defn wrap-verbose [handler]
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
