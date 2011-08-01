(ns joodo.middleware.servlet-session-spec
  (:use
    [speclj.core]
    [joodo.middleware.servlet-session])
  )

(deftype MockSession [store]
  SessionContainer
  (entry-keys [this]
    (keys @store))
  (set-entry [this key value]
    (swap! store assoc key value))
  (get-entry [this key]
    (get @store key))
  (remove-entry [this key]
    (swap! store dissoc key)))

(deftype MockRequest [container]
  SessionSource
  (get-session [this] container))

(describe "Servlet Session Wrapper"

  (with inbound (atom nil))
  (with outbound (atom {}))
  (with mock-handler
    (fn [request]
      (reset! @inbound request)
      @@outbound))
  (with wrapper (wrap-servlet-session @mock-handler))
  (with session (MockSession. (atom {})))
  (with request (MockRequest. @session))

  (it "handles session-less request"
    (let [response (@wrapper {:servlet-request @request})]
      (should= {} response))
    (should= {} (:session @@inbound)))

  (it "pulls one entry out of request"
    (set-entry @session :foo "Foo")
    (should= {} (@wrapper {:servlet-request @request}))
    (should= {:foo "Foo"} (:session @@inbound)))

  (it "puts one entry in response"
    (reset! @outbound {:session {:bar "Bar"}})
    (should= {:session {:bar "Bar"}} (@wrapper {:servlet-request @request}))
    (should= "Bar" (:bar @(.store @session))))

  (it "deletes entries when the value is set to nil"
    (set-entry @session :foo "Foo")
    (set-entry @session :bar "Bar")
    (reset! @outbound {:session {:foo nil}})
    (@wrapper {:servlet-request @request})
    (should= false (contains? @(.store @session) :foo))
    (should= "Bar" (:bar @(.store @session))))

  (it "clears the session when the :session is mapped to nil in response"
    (set-entry @session :foo "Foo")
    (set-entry @session :bar "Bar")
    (reset! @outbound {:session nil})
    (@wrapper {:servlet-request @request})
    (should= 0 (count @(.store @session))))

  )

(run-specs)