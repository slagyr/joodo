(ns joodo.middleware.locale-spec
  (:use
    [speclj.core]
    [joodo.middleware.locale]))

(describe "Locale Setting Middleware"
  (with updated-request (atom nil))
  (with mock-handler
    (fn [request]
      (reset! @updated-request request)))

  (context "associates a locale key into the request map"
    (it "first removes any locale info from the uri and loads locale info into the request map"
      (let [request-data {:uri "/fr_CA/index" :query-string "locale=en" :headers {"accept" "text/html" "accept-language" "en-us"}}
            supported-locales #{"fr_CA" "fr"}
            handler (wrap-locale @mock-handler supported-locales)]
        (handler request-data)
        (should= "fr_CA" (:locale @@updated-request))
        (should= "/index" (:uri @@updated-request))))

    (it "only loads up the locale if that locale is supported"
      (let [request-data {:uri "/fr_CA/index" :query-string "locale=en" :headers {"accept" "text/html" "accept-language" "en-us"}}
            supported-locales #{"fr"}
            handler (wrap-locale @mock-handler supported-locales)]
        (handler request-data)
        (should= "" (:locale @@updated-request))
        (should= "/fr_CA/index" (:uri @@updated-request))))

    (it "loads locale information from the query string if it wasn't supplied in the uri"
      (let [request-data {:uri "/index" :query-string "locale=en" :headers {"accept" "text/html" "accept-language" "en-us"}}
            handler (wrap-locale @mock-handler #{"en"})]
        (handler request-data)
        (should= "en" (:locale @@updated-request))))

    (it "loads locale information from the accept headers if it wasn't supplied in the uri or the query string"
      (let [request-data {:uri "/" :query-string "" :headers {"accept-language" "en_US,en;q=0.9,ja;q=0.8,fr;q=0.7,de;q=0.6,es;q=0.5"}}
            handler (wrap-locale @mock-handler #{"fr"})]
        (handler request-data)
        (should= "fr" (:locale @@updated-request)))))

  (context "helper functions"
    (it "copies the locale out of the uri"
      (should= "en_US" (locale-in-uri #{"en_US" "en"} "/en_US/index.html")))

    (it "doesn't copy the locale out of the uri if it isn't a supported locale"
      (should= nil (locale-in-uri #{"en"} "/en_US/index.html")))

    (it "copies the locale out of the query string"
      (should= "en_US" (locale-in-query-string #{"en_US" "en"} "q=5.45&locale=en_US")))

    (it "doesn't copy the locale out of the query string if it isn't a supported locale"
      (should= nil (locale-in-query-string #{"en"} "q=5.45&locale=en_US")))

    (it "grabs the highest q rated locale from the accept header"
      (should= "en_US" (locale-in-accept-header #{"en_US" "en"} "en_US,en;q=0.9,ja;q=0.8,fr;q=0.7,de;q=0.6,es;q=0.5")))
    ))

(run-specs)