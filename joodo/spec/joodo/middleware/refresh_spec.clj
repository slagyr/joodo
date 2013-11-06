(ns joodo.middleware.refresh-spec
  (:import [java.io FileNotFoundException])
  (:require [fresh.core :refer [ns-to-file]]
            [joodo.env :as env]
            [joodo.fake-handler]
            [joodo.middleware.refresh :as refresh]
            [speclj.core :refer :all]))

(describe "Refresh"

  (context "in production"

    (it "creates a handler with full symbol"
      (let [result (refresh/handler 'joodo.fake-handler/handler)]
        (should= joodo.fake-handler/handler result)))

    (it "creates a handler with default handler name"
      (let [result (refresh/handler 'joodo.fake-handler)]
        (should= joodo.fake-handler/handler result)))

    (it "creates a handler with another name"
      (let [result (refresh/handler 'joodo.fake-handler/other-handler)]
        (should= joodo.fake-handler/other-handler result)))

    (it "fails with missing namespace"
      (should-throw FileNotFoundException (refresh/handler 'no-such-namespace)))

    (it "fails with missing var"
      (should-throw Exception "No such var joodo.fake-handler/no-such-handler" (refresh/handler 'joodo.fake-handler/no-such-handler)))

    )

  (context "in development"

    (around [it]
      (with-redefs [env/development? (constantly true)
                    refresh/cache (ref {})]
        (with-out-str
          (it))))

    (with fake-handler-file (ns-to-file "joodo.fake-handler"))

    (it "creates a handler with full symbol"
      (let [result (refresh/handler 'joodo.fake-handler/handler)]
        (should-not= joodo.fake-handler/handler result)
        (should= nil (get @refresh/cache @fake-handler-file))
        (should= {:body "handler"} (result :some-request))
        (should= joodo.fake-handler/handler (get-in @refresh/cache [@fake-handler-file 'handler :handler]))))

    (it "creates a handler with another handler"
      (let [result (refresh/handler 'joodo.fake-handler/other-handler)]
        (should-not= joodo.fake-handler/other-handler result)
        (should= nil (get @refresh/cache @fake-handler-file))
        (should= {:body "other handler"} (result :some-request))
        (should= joodo.fake-handler/other-handler (get-in @refresh/cache [@fake-handler-file 'other-handler :handler]))))

    (it "removes reloaded files from the cache"
      (let [result (refresh/handler 'joodo.fake-handler/handler)]
        (result :some-request)
        (should= joodo.fake-handler/handler (get-in @refresh/cache [@fake-handler-file 'handler :handler]))

        (refresh/audit-refresh {:reloaded [@fake-handler-file]})
        (should= nil (get @refresh/cache @fake-handler-file))

        (result :some-request)
        (should= joodo.fake-handler/handler (get-in @refresh/cache [@fake-handler-file 'handler :handler]))))
    )

  )