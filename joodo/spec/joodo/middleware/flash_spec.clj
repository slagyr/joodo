(ns joodo.middleware.flash-spec
  (:use
    [speclj.core]
    [joodo.middleware.flash]))

(describe "Flash"

  (with inbound (atom nil))
  (with outbound (atom {}))
  (with mock-handler
    (fn [request]
      (reset! @inbound request)
      @@outbound))
  (with wrapper (wrap-flash @mock-handler))

  (it "handles no incoming flash"
    (should= {:session {:_flash nil}} (@wrapper {})))

  (it "reads flash from session"
    (should= {:session {:_flash nil}} (@wrapper {:session {:_flash {:a "a"}}}))
    (should= {:a "a"} (:flash @@inbound))
    (should= {} (:session @@inbound)))

  (it "writes flash to session"
    (reset! @outbound {:flash {:b "b"}})
    (should= {:session {:_flash {:b "b"}}} (@wrapper {})))

  (it "doesn't write to deleted session"
    (reset! @outbound {:session nil :flash {:c "c"}})
    (should= {:session nil} (@wrapper {}))))

(run-specs)
