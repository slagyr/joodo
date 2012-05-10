(ns ^{:doc "This namespace holds functions that make dealing with dates and times easy."}
  chee.datetime
  (:import
    [java.util Date Calendar GregorianCalendar]
    [java.text SimpleDateFormat]))

(defn now
  "Returns a Java Date Object that
  represents the current date and time"
  []
  (Date.))

(defn datetime
  "Returns a Java Date Object with the specified arguments. Expects one of the following for arguments:
  1. year month day
  2. year month day hour minute
  3. year month day hour minute second"
  ([year month day] (.getTime (GregorianCalendar. year (dec month) day)))
  ([year month day hour minute] (.getTime (GregorianCalendar. year (dec month) day hour minute)))
  ([year month day hour minute second] (.getTime (GregorianCalendar. year (dec month) day hour minute second))))

(defn before?
  "Expects two Date Objects as arguments. The function returns true if the
  first date comes before the second date and returns false otherwise."
  [^Date first ^Date second]
  (.before first second))

(defn after?
  "Expects two Date Objects as arguments. The function returns true if the
  first date comes after the second date and returns false otherwise."
  [^Date first ^Date second]
  (.after first second))

(defn between?
  "Expects the three Date Objects as arguments. The first date is the date
  being evaluated; the second date is the start date; the last date is the
  end date. The function returns true if the first date is between the start
  and end dates."
  [^Date date ^Date start ^Date end]
  (and
    (after? date start)
    (before? date end)))

(defn seconds
  "Converts seconds to milliseconds"
  [n] (* n 1000))

(defn minutes
  "Converts minutes to milliseconds"
  [n] (* n 60000))

(defn hours
  "Converts hours to milliseconds"
  [n] (* n 3600000))

(defn days
  "Converts days to milliseconds"
  [n] (* n 86400000))

(defn months
  "Converts a number into a format that the Calendar object understands to be an amount of months"
  [n] [Calendar/MONTH n])

(defn years
  "Converts a number into a format that the Calendar object understands to be an amount of years"
  [n] [Calendar/YEAR n])

(defn to-calendar
  "Converts a Date object into a GregorianCalendar object"
  [datetime]
  (doto (GregorianCalendar.)
    (.setTime datetime)))

(defn- mod-time-by-units
  "Modifies the value of a Date object. Expects the first argument to be
  a Date object, the second argument to be a vector representing the amount
  of time to be changed, and the last argument to be either a + or - (indicating
  which direction to modify time)."
  [time [unit n] direction]
  (let [calendar (GregorianCalendar.)
        n (direction n)]
    (.setTime calendar time)
    (.add calendar unit n)
    (.getTime calendar)))

(defn- mod-time
  "Modifies the value of a Date object. Expects the first argument to be
  a Date object, the second argument to be an amount of milliseconds, and
  the last argument to be either a + or - (indicating which direction to
  modify time)."
  [time bit direction]
  (cond
    (number? bit) (Date. (direction (.getTime time) bit))
    (vector? bit) (mod-time-by-units time bit direction)))

(defn before
  "Rewinds the time on a Date object. Expects a Date object as the first
  argument and a number of milliseconds to rewind time by."
  [^Date time & bits]
  (reduce #(mod-time %1 %2 -) time bits))

(defn after
  "Fast-forwards the time on a Date object. Expects a Date object as the first
  argument and a number of milliseconds to fast-forward time by."
  [^Date time & bits]
  (reduce #(mod-time %1 %2 +) time bits))

(defn seconds-ago
  "Returns a Java Date Object with a value of n seconds ago where
  n is the value passed to the function."
  [n]
  (before (now) (seconds n)))

(defn seconds-from-now
  "Returns a Java Date Object with a value of n seconds from now where
  n is the value passed to the function."
  [n]
  (after (now) (seconds n)))

(defn minutes-ago
  "Returns a Java Date Object with a value of n minutes ago where
  n is the value passed to the function."
  [n]
  (before (now) (minutes n)))

(defn minutes-from-now
  "Returns a Java Date Object with a value of n minutes from now where
  n is the value passed to the function."
  [n]
  (after (now) (minutes n)))

(defn hours-ago
  "Returns a Java Date Object with a value of n hours ago where
  n is the value passed to the function."
  [n]
  (before (now) (hours n)))

(defn hours-from-now
  "Returns a Java Date Object with a value of n hours from now where
  n is the value passed to the function."
  [n]
  (after (now) (hours n)))

(defn days-ago
  "Returns a Java Date Object with a value of n days ago where
  n is the value passed to the function."
  [n]
  (before (now) (days n)))

(defn days-from-now
  "Returns a Java Date Object with a value of n days from now where
  n is the value passed to the function."
  [n]
  (after (now) (days n)))

(defn months-ago
  "Returns a Java Date Object with a value of n months ago where
  n is the value passed to the function."
  [n]
  (before (now) (months n)))

(defn months-from-now
  "Returns a Java Date Object with a value of n months from now where
  n is the value passed to the function."
  [n]
  (after (now) (months n)))

(defn years-ago
  "Returns a Java Date Object with a value of n years ago where
  n is the value passed to the function."
  [n]
  (before (now) (years n)))

(defn years-from-now
  "Returns a Java Date Object with a value of n years from now where
  n is the value passed to the function."
  [n]
  (after (now) (years n)))

(def date-formats {
  :http (SimpleDateFormat. "EEE, dd MMM yyyy HH:mm:ss Z")
  :rfc1123 (SimpleDateFormat. "EEE, dd MMM yyyy HH:mm:ss Z")
  :iso8601 (SimpleDateFormat. "yyyy-MM-dd HH:mm:ssZ")
  :dense (SimpleDateFormat. "yyyyMMddHHmmss")
  })

(defn- to-date-formater [format]
  (cond
    (keyword? format) (format date-formats)
    (string? format) (SimpleDateFormat. format)
    (= SimpleDateFormat (class format)) format
    :else (throw (Exception. (str "Unhandled date format: " format)))))

(defn parse-datetime
  "Parses text into a Java Date object. Expects a keyword, string, or SimpleDateFormat
  object as the first object and a string representing the date as the second argument."
  [format value]
  (let [formatter (to-date-formater format)]
    (.parse formatter value)))

(defn format-datetime
  "Returns a string that is populated with a formatted date and time. Expects the
  first argument to be the requested format and the second argument to be the date
  to be formatted.
  The following are options for the first argument:
  1. Keyword - :http, :rfc1123, :iso8601, :dense
  2. String - must be a valid argument to the SimpleDateFormat Java Object
  3. SimpleDateFormat - Java Object"
  [format value]
  (if value
    (let [formatter (to-date-formater format)]
      (.format formatter value))))

(defn year
  "Returns the current year. Expects a Java Date Object."
  [datetime]
  (.get (to-calendar datetime) Calendar/YEAR))

(defn month
  "Returns the current month. Expects a Java Date Object."
  [datetime]
  (inc (.get (to-calendar datetime) Calendar/MONTH)))

(defn day
  "Returns the current day. Expects a Java Date Object."
  [datetime]
  (.get (to-calendar datetime) Calendar/DAY_OF_MONTH))


