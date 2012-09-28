(ns chee.coerce-spec
  (:use [speclj.core]
        [chee.coerce]))

(describe "Type Coercion"
  (context "Integer"
    (it "coerces a string to an integer"
      (should= (Integer. 1) (->int "1"))
      (should-throw IllegalArgumentException (->int "not an int")))

    (it "coerces a long to an integer"
      (should= (Integer. 1) (->int 1)))

    (it "coerces an integer to an integer"
      (should= (Integer. 1) (->int (Integer/parseInt "1")))))

  (context "String"
    (it "coerces a string to a string"
      (should= "thing" (->string "thing")))

    (it "coerces a symbol to a string"
      (should= "thing" (->string (symbol "thing"))))

    (it "coerces a keyword to a string"
      (should= "thing" (->string :thing)))

    (it "coerces a boolean to a string"
      (should= "true" (->string true))
      (should= "false" (->string false))))

  (context "Keyword"
    (it "coerces a string to a keyword"
      (should= :thing (->keyword "thing")))

    (it "coerces a symbol to a keyword"
      (should= :thing (->keyword (symbol "thing"))))

    (it "coerces a keyword to a keyword"
      (should= :thing (->keyword :thing)))

    (it "coerces a boolean to a keyword"
      (should= :true (->keyword true))
      (should= :false (->keyword false))))

  (context "Symbol"
    (with sym (symbol "thing"))

    (it "coerces a string to a symbol"
      (should= @sym (->symbol "thing")))

    (it "coerces a symbol to a symbol"
      (should= @sym (->symbol (symbol "thing"))))

    (it "coerces a keyword to a symbol"
      (should= @sym (->symbol :thing)))

    (it "coerces a boolean to a symbol"
      (should= (symbol "true") (->symbol true))
      (should= (symbol "false") (->symbol false))))

  (context "Boolean"
    (it "coerces a string to boolean"
      (should= true  (->bool "true"))
      (should= false (->bool "false")))

    (it "coerces nil to boolean"
      (should= false (->bool nil)))

    (it "coerces a boolean to boolean"
      (should= true  (->bool true))
      (should= false (->bool false)))

    (it "coeerces a keyword to a boolean"
      (should= true (->bool :true))
      (should= false (->bool :false)))

    (it "coerces a symbol to a boolean"
      (should= true (->bool (symbol "true"))))))
