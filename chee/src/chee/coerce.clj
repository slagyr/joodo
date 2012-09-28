(ns chee.coerce)

(defprotocol AsBoolean
  (->bool [this]))

(defprotocol AsInteger
  (->int [this]))

(defprotocol AsString
  (->string [this]))

(defprotocol AsKeyword
  (->keyword [this]))

(defprotocol AsSymbol
  (->symbol [this]))

(extend-type java.lang.Long
  AsInteger
  (->int [this] (.intValue this)))

(extend-type java.lang.Integer
  AsInteger
  (->int [this] this))

(extend-type java.lang.String
  AsString
  (->string [this] this)

  AsKeyword
  (->keyword [this] (keyword this))

  AsBoolean
  (->bool [this] (= "true" this))

  AsInteger
  (->int [this] (Integer/parseInt this))

  AsSymbol
  (->symbol [this] (symbol this)))

(extend-type clojure.lang.Keyword
  AsString
  (->string [this] (name this))

  AsKeyword
  (->keyword [this] this)

  AsBoolean
  (->bool [this] (= :true this))

  AsSymbol
  (->symbol [this] (symbol (->string this))))

(extend-type clojure.lang.Symbol
  AsString
  (->string [this] (name this))

  AsKeyword
  (->keyword [this] (keyword (name this)))

  AsBoolean
  (->bool [this] (= "true" (name this)))

  AsSymbol
  (->symbol [this] this))

(extend-type java.lang.Boolean
  AsBoolean
  (->bool [this] this)

  AsString
  (->string [this] (.toString this))

  AsKeyword
  (->keyword [this] (keyword (.toString this)))

  AsSymbol
  (->symbol [this] (symbol (->string this))))

(extend-type nil
  AsBoolean
  (->bool [this] false))
