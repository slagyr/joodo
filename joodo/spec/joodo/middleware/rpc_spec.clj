(ns joodo.middleware.rpc-spec
  (:require [speclj.core :refer :all]
            [joodo.middleware.rpc :refer :all]))

(defn ^:remote echo [& data]
  data)

(defn ^:remote lazy-printer [& data]
  (map println data))

(defn ^:remote meta-map [& data]
  (with-meta {} {:response (first data)}))

(defn non-remote [& data]
  :forbidden-treasure)

(describe "RPC"

  (defn rpc-request [remote args]
    {:request-method :post
     :uri "/_rpc"
     :params {:params (pr-str args)
              :remote remote}})

  (with handler (wrap-rpc (fn [_] :pass-through)))

  (it "calls a remote method"
    (should-invoke echo {:with [1 2 3] :return :foo}
      (let [response (@handler (rpc-request 'joodo.middleware.rpc-spec/echo [1 2 3]))]
        (should= 202 (:status response)) ; Accepted
        (should= "application/edn; charset=utf-8" (get-in response [:headers "Content-Type"]))
        (should= ":foo" (:body response)))))

  (it "requires post method"
    (should= :pass-through (@handler (assoc (rpc-request 'joodo.middleware.rpc-spec/echo [:foo]) :request-method :get))))

  (it "requires /_rpc uri"
    (should= :pass-through (@handler (assoc (rpc-request 'joodo.middleware.rpc-spec/echo [:foo]) :uri "/blah"))))

  (it "return null when remote not found"
    (should= not-found (@handler (rpc-request 'joodo.middleware.rpc-spec/missing [:foo]))))

  (it "doesn't error in missing namespace"
    (should= not-found (@handler (rpc-request 'oh.noez/missing [:foo]))))

  (it "only invokes ^:remote vars"
    (should= not-found (@handler (rpc-request 'joodo.middleware.rpc-spec/non-remote [:foo]))))

  (it "does't let lazy prints get in the way"
    (with-out-str
      (should= "(nil nil nil)"
        (:body (@handler (rpc-request 'joodo.middleware.rpc-spec/lazy-printer [1 2 3]))))))

  (it "alternative URI"
    (let [handler (wrap-rpc (fn [_] :pass-through) :uri "/lol")]
      (should-invoke echo {:with [:foo]}
        (handler (assoc (rpc-request 'joodo.middleware.rpc-spec/echo [:foo]) :uri "/lol")))))

  (it "namespace prefix"
    (let [handler (wrap-rpc (fn [_] :pass-through) :prefix 'joodo.middleware.)]
      (should-invoke echo {:with [:foo]}
        (handler (rpc-request 'rpc-spec/echo [:foo])))))

  (it "can add rpc middleware"
    (let [nester (fn [handler] (fn [remote request params] (handler remote request [params])))
          whammy (fn [handler] (fn [remote request params] (assoc (handler remote request params) :stop :whammy)))
          handler (wrap-rpc (fn [_] :pass-through) :middleware [nester whammy])
          response (handler (rpc-request 'joodo.middleware.rpc-spec/echo [:foo]))]
      (should= "([:foo])" (:body response))
      (should= :whammy (:stop response))))

  (it "adds meta data to response"
    (let [response (@handler (rpc-request 'joodo.middleware.rpc-spec/meta-map [{:foo :bar :fizz :bang}]))]
      (should= :bar (:foo response))
      (should= :bang (:fizz response))))

  )
