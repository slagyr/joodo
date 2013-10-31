(ns {{name}}.main
  (:require-macros [hiccups.core :as h])
  (:require [domina :as dom]
            [domina.css :as css]
            [domina.events :as event]
            [shoreleave.remote :refer [remote-callback]]))


(defn foo []
  :foo)
