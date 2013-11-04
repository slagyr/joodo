(ns chee.matchers
  (:require [speclj.core :refer :all]))

(defmacro should=str [expected-form actual-form]
  `(do
     (should= ~expected-form ~actual-form)
     (should-be-a java.lang.String ~actual-form)))

(defmacro should=kwd [expected-form actual-form]
  `(do
     (should= ~expected-form ~actual-form)
     (should-be-a clojure.lang.Keyword ~actual-form)))

(defmacro should=sym [expected-form actual-form]
  `(do
     (should= ~expected-form ~actual-form)
     (should-be-a clojure.lang.Symbol ~actual-form)))

(defmacro should=bool [expected-form actual-form]
  `(do
     (should= ~expected-form ~actual-form)
     (should-be-a java.lang.Boolean ~actual-form)))

(defmacro should=byte [expected-form actual-form]
  `(do
     (should= ~expected-form ~actual-form)
     (should-be-a java.lang.Byte ~actual-form)))

(defmacro should=short [expected-form actual-form]
  `(do
     (should= ~expected-form ~actual-form)
     (should-be-a java.lang.Short ~actual-form)))

(defmacro should=int [expected-form actual-form]
  `(do
     (should= ~expected-form ~actual-form)
     (should-be-a java.lang.Integer ~actual-form)))

(defmacro should=long [expected-form actual-form]
  `(do
     (should= ~expected-form ~actual-form)
     (should-be-a java.lang.Long ~actual-form)))

(defmacro should=biginteger [expected-form actual-form]
  `(do
     (should= ~expected-form ~actual-form)
     (should-be-a java.math.BigInteger ~actual-form)))

(defmacro should=flt [expected-form actual-form]
  `(do
     (should= ~expected-form ~actual-form)
     (should-be-a java.lang.Float ~actual-form)))

(defmacro should=dbl [expected-form actual-form]
  `(do
     (should= ~expected-form ~actual-form)
     (should-be-a java.lang.Double ~actual-form)))

