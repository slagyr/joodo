(ns chee.coerce)

(defprotocol AsBoolean
  (->bool [this]))

(defprotocol AsString
  (->string [this]))

(defprotocol AsKeyword
  (->keyword [this]))

(defprotocol AsSymbol
  (->symbol [this]))

(defprotocol AsByte
  (->byte [this]))

(defprotocol AsShort
  (->short [this]))

(defprotocol AsInteger
  (->int [this]))

(defprotocol AsLong
  (->long [this]))

(defprotocol AsBigInteger
  (->biginteger [this]))

(defprotocol AsFloat
  (->float [this]))

(defprotocol AsDouble
  (->double [this]))

(extend-type java.lang.Byte
  AsByte
  (->byte [this] this)

  AsShort
  (->short [this] (.shortValue this))

  AsInteger
  (->int [this] (.intValue this))

  AsLong
  (->long [this] (.longValue this))

  AsBigInteger
  (->biginteger [this] (BigInteger. (str this)))

  AsFloat
  (->float [this] (.floatValue this))

  AsDouble
  (->double [this] (.doubleValue this))

  )

(extend-type java.lang.Short
  AsByte
  (->byte [this] (.byteValue this))

  AsShort
  (->short [this] this)

  AsInteger
  (->int [this] (.intValue this))

  AsLong
  (->long [this] (.longValue this))

  AsBigInteger
  (->biginteger [this] (BigInteger. (str this)))

  AsFloat
  (->float [this] (.floatValue this))

  AsDouble
  (->double [this] (.doubleValue this))

  )

(extend-type java.lang.Integer
  AsBoolean
  (->bool [this] (= this 1))

  AsByte
  (->byte [this] (.byteValue this))

  AsShort
  (->short [this] (.shortValue this))

  AsInteger
  (->int [this] this)

  AsLong
  (->long [this] (.longValue this))

  AsBigInteger
  (->biginteger [this] (BigInteger. (str this)))

  AsFloat
  (->float [this] (.floatValue this))

  AsDouble
  (->double [this] (.doubleValue this))

  )

(extend-type java.lang.Long
  AsBoolean
  (->bool [this] (= this 1))

  AsByte
  (->byte [this] (.byteValue this))

  AsShort
  (->short [this] (.shortValue this))

  AsInteger
  (->int [this] (.intValue this))

  AsLong
  (->long [this] this)

  AsBigInteger
  (->biginteger [this] (BigInteger. (str this)))

  AsFloat
  (->float [this] (.floatValue this))

  AsDouble
  (->double [this] (.doubleValue this))

  )

(extend-type java.math.BigInteger
  AsBoolean
  (->bool [this] (= this 1))

  AsByte
  (->byte [this] (.byteValue this))

  AsShort
  (->short [this] (.shortValue this))

  AsInteger
  (->int [this] (.intValue this))

  AsLong
  (->long [this] (.longValue this))

  AsBigInteger
  (->biginteger [this] this)

  AsFloat
  (->float [this] (.floatValue this))

  AsDouble
  (->double [this] (.doubleValue this))

  )

(extend-type java.lang.Float
  AsFloat
  (->float [this] this)

  AsDouble
  (->double [this] (.doubleValue this))

  )

(extend-type java.lang.Double
  AsFloat
  (->float [this] (.floatValue this))

  AsDouble
  (->double [this] this)

  )

(extend-type java.lang.String
  AsBoolean
  (->bool [this] (= "true" this))

  AsKeyword
  (->keyword [this] (keyword this))

  AsString
  (->string [this] this)

  AsSymbol
  (->symbol [this] (symbol this))

  AsByte
  (->byte [this] (Byte. this))

  AsShort
  (->short [this] (Short. this))

  AsInteger
  (->int [this] (Integer. this))

  AsLong
  (->long [this] (Long. this))

  AsBigInteger
  (->biginteger [this] (BigInteger. this))

  AsFloat
  (->float [this] (Float. this))

  AsDouble
  (->double [this] (Double. this))

  )
(extend-type clojure.lang.Keyword
  AsString
  (->string [this] (name this))

  AsKeyword
  (->keyword [this] this)

  AsBoolean
  (->bool [this] (= :true this))

  AsSymbol
  (->symbol [this] (symbol (name this))))

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
  (->symbol [this] (symbol (.toString this))))

(extend-type nil
  AsBoolean
  (->bool [this] nil)

  AsString
  (->string [this] nil)

  AsKeyword
  (->keyword [this] nil)

  AsSymbol
  (->symbol [this] nil)

  AsByte
  (->byte [this] nil)

  AsShort
  (->short [this] nil)

  AsInteger
  (->int [this] nil)

  AsLong
  (->long [this] nil)

  AsBigInteger
  (->biginteger [this] nil)

  AsFloat
  (->float [this] nil)

  AsDouble
  (->double [this] nil)

  )
