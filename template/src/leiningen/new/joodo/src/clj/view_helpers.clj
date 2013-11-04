(ns {{name}}.view-helpers
   "Put helper functions for views in this namespace."
   (:require [hiccup.element :refer :all]
             [hiccup.form :refer :all]
             [hiccup.page :refer :all]
             [joodo.env :as env]
             [joodo.views :refer [render-partial *view-context*]]))
