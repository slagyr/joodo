(ns joodo.datetime-spec
  (:use
    [speclj.core :exclude (before after)]
    [joodo.datetime])
  (:import
    [java.util Date]
    [java.text SimpleDateFormat]))

(describe "Dates"

  (it "easily creates new dates with current time"
    (let [now (now)]
      (should= Date (.getClass now))
      (should (> 100 (- (System/currentTimeMillis) (.getTime now))))))

  (it "easily creates date of specified time"
    (should= "20110101000000" (format-datetime :dense (datetime 2011 1 1)))
    (should= "20110101023400" (format-datetime :dense (datetime 2011 1 1 2 34)))
    (should= "20110101023456" (format-datetime :dense (datetime 2011 1 1 2 34 56))))

  (it "compares dates"
    (should= true (before? (datetime 2011 1 1) (datetime 2011 1 2)))
    (should= false (before? (datetime 2011 1 3) (datetime 2011 1 2)))
    (should= false (after? (datetime 2011 1 1) (datetime 2011 1 2)))
    (should= true (after? (datetime 2011 1 3) (datetime 2011 1 2))))

  (it "checks if a date is btween two other dates"
    (should= true (between? (datetime 2011 1 2) (datetime 2011 1 1) (datetime 2011 1 3)))
    (should= false (between? (datetime 2011 1 3) (datetime 2011 1 2) (datetime 2011 1 1))))

  (it "creates dates relative to now in second increments"
    (should= true (before? (seconds-ago 1) (now)))
    (should= true (before? (seconds-ago 2) (seconds-ago 1)))
    (should= true (after? (seconds-from-now 1) (now)))
    (should= true (after? (seconds-from-now 2) (seconds-from-now 1))))

  (it "creates dates relative to now in minute increments"
    (should= true (before? (minutes-ago 1) (now)))
    (should= true (before? (minutes-ago 1) (seconds-ago 59)))
    (should= false (before? (minutes-ago 1) (seconds-ago 61)))
    (should= true (after? (minutes-from-now 1) (now)))
    (should= true (after? (minutes-from-now 1) (seconds-from-now 59)))
    (should= false (after? (minutes-from-now 1) (seconds-from-now 61))))

  (it "creates dates relative to now in hour increments"
    (should= true (before? (hours-ago 1) (now)))
    (should= true (before? (hours-ago 1) (minutes-ago 59)))
    (should= false (before? (hours-ago 1) (minutes-ago 61)))
    (should= true (after? (hours-from-now 1) (now)))
    (should= true (after? (hours-from-now 1) (minutes-from-now 59)))
    (should= false (after? (hours-from-now 1) (minutes-from-now 61))))

  (it "creates dates relative to now in day increments"
    (should= true (before? (days-ago 1) (now)))
    (should= true (before? (days-ago 1) (hours-ago 23)))
    (should= false (before? (days-ago 1) (hours-ago 25)))
    (should= true (after? (days-from-now 1) (now)))
    (should= true (after? (days-from-now 1) (hours-from-now 23)))
    (should= false (after? (days-from-now 1) (hours-from-now 25))))

  (it "create dates relative to other dates by month increment"
    (should= "20110201000000" (format-datetime :dense  (after (datetime 2011 1 1) (months 1))))
    (should= "20101201000000" (format-datetime :dense  (before (datetime 2011 1 1) (months 1))))
    (should= true (after? (months-from-now 1) (days-from-now 27)))
    (should= false (after? (months-from-now 1) (days-from-now 32)))
    (should= true (before? (months-ago 1) (days-ago 27)))
    (should= false (before? (months-ago 1) (days-ago 32))))

  (it "create dates relative to other dates by year increment"
    (should= "20120101000000" (format-datetime :dense  (after (datetime 2011 1 1) (years 1))))
    (should= "20100101000000" (format-datetime :dense  (before (datetime 2011 1 1) (years 1))))
    (should= true (after? (years-from-now 1) (months-from-now 11)))
    (should= false (after? (years-from-now 1) (months-from-now 13)))
    (should= true (before? (years-ago 1) (months-ago 11)))
    (should= false (before? (years-ago 1) (months-ago 13))))

  (it "parses and formats dates in HTTP format"
    (let [date (parse-datetime :http "Sun, 06 Nov 1994 08:49:37 GMT")]
      (should= true (after? date (datetime 1994 11 5)))
      (should= true (before? date (datetime 1994 11 7)))
;      (should= "Sun, 06 Nov 1994 02:49:37 -0600" (format-datetime :http date)) ; only works in certain CST zone
      ))

  (it "parses and formats dates in custom format"
    (let [date (parse-datetime "MMM d, yyyy HH:mm" "Nov 6, 1994 08:49")]
      (should= "Nov 6, 1994 08:49" (format-datetime "MMM d, yyyy HH:mm" date))))

  (it "parses and formats dates in custom format object"
    (let [format (SimpleDateFormat. "MMM d, yyyy HH:mm")
          date (parse-datetime format "Nov 6, 1994 08:49")]
      (should= "Nov 6, 1994 08:49" (format-datetime format date))))

  (it "parses and formats dates in ISO 8601 format"
    (let [date (parse-datetime :iso8601 "1994-11-06 08:49:12 GMT")]
      (should= "1994-11-06 08:49:12+0000" (format-datetime :iso8601 date))))

  (it "should know the year of a date"
    (should= 2011 (year (datetime 2011 1 1)))
    (should= 2010 (year (datetime 2010 1 1)))
    (should= 2009 (year (datetime 2009 12 31)))
    (should= 1976 (year (datetime 1976 7 4))))

  (it "should know the month of a date"
    (should= 1 (month (datetime 2011 1 1)))
    (should= 12 (month (datetime 2009 12 31))))

  (it "should know the day of a date"
    (should= 1 (day (datetime 2011 1 1)))
    (should= 31 (day (datetime 2009 12 31))))
  )

(run-specs)



