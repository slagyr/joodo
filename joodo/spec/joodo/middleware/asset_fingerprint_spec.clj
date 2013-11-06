(ns joodo.middleware.asset-fingerprint-spec
  (:require [speclj.core :refer :all]
            [joodo.middleware.asset-fingerprint :refer :all ]))


(describe "asset fingerprint"

  (it "adds an md5 to a path"
    (should= "/some/path.abc123.xyz" (add-checksum-to-path "abc123" "/some/path.xyz"))
    (should= "/some/path.abc456.xyz" (add-checksum-to-path "abc456" "/some/path.xyz"))
    (should= "/some/other.abc456.abc" (add-checksum-to-path "abc456" "/some/other.abc"))
    (should= "/some/other.else.abc456.abc" (add-checksum-to-path "abc456" "/some/other.else.abc"))
    (should= "/some/extensionless.abc123" (add-checksum-to-path "abc123" "/some/extensionless"))
    (should= "/some.thing/extensionless.abc123" (add-checksum-to-path "abc123" "/some.thing/extensionless"))
    (should= "extensionless.abc123" (add-checksum-to-path "abc123" "extensionless"))
    (should= "file.abc123.xyz" (add-checksum-to-path "abc123" "file.xyz")))

  (it "removes fingerprint from path"
    (should= "/some/path.xyz" (remove-checksum-from-path "/some/path.abc123.xyz"))
    (should= "/some/path.xyz" (remove-checksum-from-path "/some/path.abc456.xyz"))
    (should= "/some/other.abc" (remove-checksum-from-path "/some/other.abc456.abc"))
    (should= "/some/other.else.abc" (remove-checksum-from-path "/some/other.else.abc456.abc"))
    (should= "/some/extensionless" (remove-checksum-from-path "/some/extensionless.abc123"))
    (should= "/some.thing/extensionless" (remove-checksum-from-path "/some.thing/extensionless.abc123"))
    (should= "extensionless" (remove-checksum-from-path "extensionless.abc123"))
    (should= "file.xyz" (remove-checksum-from-path "file.abc123.xyz")))

  (it "adds checksum to path in classpath"
    (let [path "joodo/middleware/asset_fingerprint_spec.clj"
          result (path-with-fingerprint "joodo/middleware/asset_fingerprint_spec.clj")]
      (should-not= path result)
      (should= path (path-without-fingerprint result))))

  (it "ignored requests without finger prints"
    (let [request {:stuff :blah :uri "/path/without/fingerprint.abc"}]
      (should= request (resolve-fingerprint-in request))))

  (it "resolves fingerprinted assets in request"
    (let [fingerprint "abcdefghijklmnopqrstuvwxyz123456"
          request {:stuff :blah :uri (str "/path/with/fingerprint." fingerprint ".abc")}]
      (should=
        {:stuff :blah :uri "/path/with/fingerprint.abc"}
        (resolve-fingerprint-in request))))

  (it "middleware passes resolved requests"
    (let [uri (atom nil)
          inner-handler (fn [request] (reset! uri (:uri request)))
          wrapped-handler (wrap-asset-fingerprint inner-handler)
          fingerprint "abcdefghijklmnopqrstuvwxyz123456"
          request {:uri (str "/path/with/fingerprint." fingerprint ".abc")}]
      (wrapped-handler request)
      (should= "/path/with/fingerprint.abc" @uri))))






