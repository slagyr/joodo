(ns joodo.datetime
  (:import
    [java.util Date Calendar GregorianCalendar]
    [java.text SimpleDateFormat]))

(defn now []
  (Date.))

(defn datetime
  ([year month day] (.getTime (GregorianCalendar. year (dec month) day)))
  ([year month day hour minute] (.getTime (GregorianCalendar. year (dec month) day hour minute)))
  ([year month day hour minute second] (.getTime (GregorianCalendar. year (dec month) day hour minute second))))

(defn before? [^Date first ^Date second]
  (.before first second))

(defn after? [^Date first ^Date second]
  (.after first second))

(defn between? [^Date date ^Date start ^Date end]
  (and
    (after? date start)
    (before? date end)))

(defn seconds [n] (* n 1000))
(defn minutes [n] (* n 60000))
(defn hours [n] (* n 3600000))
(defn days [n] (* n 86400000))
(defn months [n] [Calendar/MONTH n])
(defn years [n] [Calendar/YEAR n])

(defn to-calendar [datetime]
  (doto (GregorianCalendar.)
    (.setTime datetime)))

(defn- mod-time-by-units [time [unit n] direction]
  (let [calendar (GregorianCalendar.)
        n (direction n)]
    (.setTime calendar time)
    (.add calendar unit n)
    (.getTime calendar)))

(defn- mod-time [time bit direction]
  (cond
    (number? bit) (Date. (direction (.getTime time) bit))
    (vector? bit) (mod-time-by-units time bit direction)))

(defn before [^Date time & bits]
  (reduce #(mod-time %1 %2 -) time bits))

(defn after [^Date time & bits]
  (reduce #(mod-time %1 %2 +) time bits))

(defn seconds-ago [n]
  (before (now) (seconds n)))

(defn seconds-from-now [n]
  (after (now) (seconds n)))

(defn minutes-ago [n]
  (before (now) (minutes n)))

(defn minutes-from-now [n]
  (after (now) (minutes n)))

(defn hours-ago [n]
  (before (now) (hours n)))

(defn hours-from-now [n]
  (after (now) (hours n)))

(defn days-ago [n]
  (before (now) (days n)))

(defn days-from-now [n]
  (after (now) (days n)))

(defn months-ago [n]
  (before (now) (months n)))

(defn months-from-now [n]
  (after (now) (months n)))

(defn years-ago [n]
  (before (now) (years n)))

(defn years-from-now [n]
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

(defn parse-datetime [format value]
  (let [formatter (to-date-formater format)]
    (.parse formatter value)))

(defn format-datetime [format value]
  (if value
    (let [formatter (to-date-formater format)]
      (.format formatter value))))

(defn year [datetime]
  (.get (to-calendar datetime) Calendar/YEAR))

(defn month [datetime]
  (inc (.get (to-calendar datetime) Calendar/MONTH)))

(defn day [datetime]
  (.get (to-calendar datetime) Calendar/DAY_OF_MONTH))


