(ns joodo.spec-helpers.controller
  (:use
    [speclj.core]
    [joodo.views :only (*view-context* render-template render-html)]
    [joodo.datetime :only (minutes-from-now)]
    [joodo.middleware.request :only (*request*)]))

(declare *routes*)

(defn with-routes [routes]
  (around [it]
    (binding [*routes* routes]
      (it))))

(defn request [method resource & extras]
  (let [request {:request-method method :uri resource}
        request (if (seq extras) (apply assoc request extras) request)]
    (binding [*request* request]
      (*routes* request))))

(defn do-get [resource & extras]
  (apply request :get resource extras))

(defn do-post [resource & extras]
  (apply request :post resource extras))

(def rendered-template (atom nil))
(def rendered-html (atom nil))
(def rendered-context (atom nil))

(defn mock-render-template [template & args]
  (reset! rendered-template template)
  (reset! rendered-context (merge *view-context* (apply hash-map args)))
  (str template))

(defn mock-render-html [html & args]
  (reset! rendered-html html)
  (reset! rendered-context (merge *view-context* (apply hash-map args)))
  html)

(defn with-mock-rendering []

  [(before (reset! rendered-template nil))

   (around [it]
     (binding [render-template mock-render-template
               render-html mock-render-html]
       (it)))])

(defmacro should-redirect-to [response location]
  `(do
    (should= 302 (:status ~response))
    (should= ~location ((:headers ~response) "Location"))))
