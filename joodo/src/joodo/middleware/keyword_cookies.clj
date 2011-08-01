(ns joodo.middleware.keyword-cookies)

(defn- keyify [target]
  (cond
    (map? target)
      (into {}
        (for [[k v] target]
          [(keyword k) (keyify v)]))
    (vector? target)
      (vec (map keyify target))
    :else
      target))

(defn wrap-keyword-cookies
  [handler]
  (fn [req]
    (handler (update-in req [:cookies] keyify))))
