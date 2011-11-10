(ns joodo.middleware.servlet-session
  "Adds session functionality that's integrated with the ServletSession")

(defprotocol SessionContainer
  (entry-keys [this])
  (set-entry [this key value])
  (get-entry [this key])
  (remove-entry [this key]))

(defprotocol SessionSource
  (get-session [this]))

(defn- enumeration->seq [enumeration]
  (loop [result []]
    (if (.hasMoreElements enumeration)
      (recur (conj result (.nextElement enumeration)))
      result)))

(extend-type javax.servlet.http.HttpSession
  SessionContainer
  (entry-keys [this]
    (map keyword (enumeration->seq (.getAttributeNames this))))
  (set-entry [this key value]
    (.setAttribute this (name key) value))
  (get-entry [this key]
    (.getAttribute this (name key)))
  (remove-entry [this key]
    (.removeAttribute this (name key))))

(extend-type javax.servlet.http.HttpServletRequest
  SessionSource
  (get-session [this]
    (.getSession this)))

(defn- enumeration->seq [enumeration]
  (loop [result []]
    (if (.hasMoreElements enumeration)
      (recur (conj result (.nextElement enumeration)))
      result)))

(defn- read-session [request]
  (let [http-session (get-session (:servlet-request request))
        keys (entry-keys http-session)]
    (reduce #(assoc %1 %2 (get-entry http-session %2)) {} keys)))

(defn- clear-session [http-session]
  (let [keys (entry-keys http-session)]
    (doseq [key keys]
      (remove-entry http-session key))))

(defn- write-session [request response]
  (let [session-map (:session response)
        http-session (get-session (:servlet-request request))]
    (if (and (= nil session-map) (contains? response :session))
      (clear-session http-session)
      (doseq [[key value] session-map]
        (if (nil? value)
          (remove-entry http-session key)
          (set-entry http-session key value))))))

(defn wrap-servlet-session [handler]
  (fn [request]
    (let [session-map (read-session request)
          response (handler (assoc request :session session-map))]
      (write-session request response)
      response)))
