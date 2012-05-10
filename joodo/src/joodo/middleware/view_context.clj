(ns ^{:doc "This namespace contains logic that incorporates a var called *view-context* with the ring-handler."}
  joodo.middleware.view-context
  (:use
    [joodo.views :only (*view-context*)]
    [chee.util :only (->options)]))

(defn wrap-view-context
  "Middleware that configures the *view-context* for rendering.  The first argument is the wrapped
  ring handler.  The following argument will the converted to a hashmap and merged with the
  *view-contact* in a new binding.  Optionally the second parameter may be a map that will
  be merged with the rest of the args and the *view-context*."
  [handler & kwargs]
  (let [options (->options kwargs)
        view-context (merge *view-context* options)]
    (fn [request]
      (binding [*view-context* view-context]
        (handler request)))))