(ns chee.coerce-spec
  (:require [speclj.core :refer :all]
            [chee.coerce :refer :all]))

(defmacro should=== [expected-form actual-form]
  `(do
     (should= ~expected-form ~actual-form)
     (should-be-a (class ~expected-form) ~actual-form)))

(describe "chee.coerce"

  (context "java.lang.String"

    (it "coerces a string to a string"
      (should=== "thing" (->string "thing")))

    (it "coerces a symbol to a string"
      (should=== "thing" (->string (symbol "thing"))))

    (it "coerces a keyword to a string"
      (should=== "thing" (->string :thing)))

    (it "coerces a boolean to a string"
      (should=== "true" (->string true))
      (should=== "false" (->string false)))

    (it "handles nil"
      (should-be-nil (->string nil)))

    )

  (context "clojure.lang.Keyword"

    (it "coerces a string to a keyword"
      (should=== :thing (->keyword "thing")))

    (it "coerces a symbol to a keyword"
      (should=== :thing (->keyword (symbol "thing"))))

    (it "coerces a keyword to a keyword"
      (should=== :thing (->keyword :thing)))

    (it "coerces a boolean to a keyword"
      (should=== :true (->keyword true))
      (should=== :false (->keyword false)))

    (it "handles nil"
      (should-be-nil (->keyword nil)))

    )

  (context "clojure.lang.Symbol"

    (with sym (symbol "thing"))

    (it "coerces a string to a symbol"
      (should=== @sym (->symbol "thing")))

    (it "coerces a symbol to a symbol"
      (should=== @sym (->symbol (symbol "thing"))))

    (it "coerces a keyword to a symbol"
      (should=== @sym (->symbol :thing)))

    (it "coerces a boolean to a symbol"
      (should=== (symbol "true") (->symbol true))
      (should=== (symbol "false") (->symbol false)))

    (it "handles nil"
      (should-be-nil (->symbol nil)))

    )

  (context "java.lang.Boolean"

    (it "coerces a string to boolean"
      (should=== true  (->bool "true"))
      (should=== false (->bool "false")))

    (it "coerces an integer to a boolean"
      (should=== true  (->bool (int 1)))
      (should=== false (->bool (int 0))))

    (it "coerces a long to a boolean"
      (should=== true  (->bool (long 1)))
      (should=== false (->bool (long 0))))

    (it "coerces a big integer to a boolean"
      (should=== true  (->bool (BigInteger. "1")))
      (should=== false (->bool (BigInteger. "0"))))

    (it "coerces a boolean to boolean"
      (should=== true  (->bool true))
      (should=== false (->bool false)))

    (it "coeerces a keyword to a boolean"
      (should=== true (->bool :true))
      (should=== false (->bool :false)))

    (it "coerces a symbol to a boolean"
      (should=== true (->bool (symbol "true"))))

    (it "handles nil"
      (should-be-nil (->bool nil)))

  )

  (context "java.lang.Byte"

    (it "coerces a string to a byte"
      (should=== (byte 1) (->byte "1"))
      (should-throw IllegalArgumentException (->byte "not an int")))

    (it "coerces a byte to a byte"
      (should=== (byte 1) (->byte (byte 1))))

    (it "coerces a short to a byte"
      (should=== (byte 1) (->byte (short 1))))

    (it "coerces a long to an byte"
      (should=== (byte 1) (->byte (long 1))))

    (it "coerces an integer to a byte"
      (should=== (byte 1) (->byte (int 1))))

    (it "coerces a big integer to a byte"
      (should=== (byte 1) (->byte (BigInteger. "1"))))

    (it "handles nil"
      (should-be-nil (->byte nil)))

    )

  (context "java.lang.Short"

    (it "coerces a string to a short"
      (should=== (short 1) (->short "1"))
      (should-throw IllegalArgumentException (->short "not an int")))

    (it "coerces a byte to a short"
      (should=== (short 1) (->short (byte 1))))

    (it "coerces a short to a short"
      (should=== (short 1) (->short (short 1))))

    (it "coerces an integer to a short"
      (should=== (short 1) (->short (int 1))))

    (it "coerces a long to an short"
      (should=== (short 1) (->short (long 1))))

    (it "coerces a big integer to a short"
      (should=== (short 1) (->short (BigInteger. "1"))))

    (it "handles nil"
      (should-be-nil (->short nil)))

    )

  (context "java.lang.Integer"

    (it "coerces a string to an integer"
      (should=== (int 1) (->int "1"))
      (should-throw IllegalArgumentException (->int "not an int")))

    (it "coerces a byte to an integer"
      (should=== (int 1) (->int (byte 1))))

    (it "coerces a short to an integer"
      (should=== (int 1) (->int (short 1))))

    (it "coerces an integer to an integer"
      (should=== (int 1) (->int (int 1))))

    (it "coerces a long to an integer"
      (should=== (int 1) (->int 1)))

    (it "coerces a biginteger to an integer"
      (should=== (int 1) (->int (BigInteger. "1"))))

    (it "handles nil"
      (should-be-nil (->int nil)))

    )

  (context "java.lang.Long"

    (it "coerces a string to a long"
      (should=== 1 (->long "1"))
      (should-throw IllegalArgumentException (->long "not an int")))

    (it "coerces a byte to a long"
      (should=== 1 (->long (byte 1))))

    (it "coerces a short to a long"
      (should=== 1 (->long (short 1))))

    (it "coerces a integer to a long"
      (should=== 1 (->long (int 1))))

    (it "coerces a long to a long"
      (should=== 1 (->long (long 1))))

    (it "coerces a big integer to a long"
      (should=== 1 (->long (BigInteger. "1"))))

    (it "handles nil"
      (should-be-nil (->long nil)))

    )

  (context "java.lang.BigInteger"

    (with big-int (BigInteger. "1"))

    (it "coerces a string to a big int"
      (should=== @big-int (->biginteger "1"))
      (should-throw IllegalArgumentException (->biginteger "not an int")))

    (it "coerces a byte to a big int"
      (should=== @big-int (->biginteger (byte 1))))

    (it "coerces a short to a big int"
      (should=== @big-int (->biginteger (short 1))))

    (it "coerces a integer to a big int"
      (should=== @big-int (->biginteger (int 1))))

    (it "coerces a long to a big int"
      (should=== @big-int (->biginteger (long 1))))

    (it "coerces a big integer to a big int"
      (should=== @big-int (->biginteger (BigInteger. "1"))))

    (it "handles nil"
      (should-be-nil (->biginteger nil)))

    )

  (context "java.lang.Float"

    (it "coerces a string to a float"
      (should=== (float 1) (->float "1"))
      (should-throw IllegalArgumentException (->float "not an int")))

    (it "coerces a byte to a float"
      (should=== (float 1) (->float (byte 1))))

    (it "coerces a short to a float"
      (should=== (float 1) (->float (short 1))))

    (it "coerces an integer to a float"
      (should=== (float 1) (->float (int 1))))

    (it "coerces a long to a float"
      (should=== (float 1) (->float (long 1))))

    (it "coerces a float to a float"
      (should=== (float 1.0) (->float (float 1.0))))

    (it "coerces a double to a float"
      (should=== (float 1) (->float (double 1))))

    (it "coerces a big integer to a float"
      (should=== (float 1) (->float (BigInteger. "1"))))

    (it "handles nil"
      (should-be-nil (->float nil)))

    )

  (context "java.lang.Double"

    (it "coerces a string to a double"
      (should=== (double 1.2) (->double "1.2"))
      (should-throw IllegalArgumentException (->double "not an int")))

    (it "coerces a byte to a double"
      (should=== (double 1) (->double (byte 1))))

    (it "coerces a short to a double"
      (should=== (double 1) (->double (short 1))))

    (it "coerces an integer to a double"
      (should=== (double 1) (->double (int 1))))

    (it "coerces a long to a double"
      (should=== (double 1) (->double (long 1))))

    (it "coerces a float to a double"
      (should=== (double 1.0) (->double (float 1.0))))

    (it "coerces a double to a double"
      (should=== (double 1) (->double (double 1))))

    (it "coerces a big integer to a double"
      (should=== (double 1) (->double (BigInteger. "1"))))

    (it "handles nil"
      (should-be-nil (->double nil)))

    )
  )
