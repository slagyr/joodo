(ns chee.pretty-map)

(declare ^:dynamic *buffer*)

(def endl (System/getProperty "line.separator"))
(def spaces (repeat " "))
(def indents (repeat "    "))

(defn- << [& values]
  (doseq [value values]
    (.append *buffer* value)))

(defn- left-col [value width]
  (let [space-count (- width (count (str value)))]
    (apply str value (take space-count spaces))))

(defn- indentation [indent]
  (let [n (if (< indent 0) 0 indent)]
    (apply str (take n indents))))

(declare make-pretty)

(defn- <<-map [the-map indent]
  (if (< (count the-map) 2)
    (<< the-map)
    (do
      (<< "{")
      (let [key-map (reduce #(assoc %1 (str %2) %2) {} (keys the-map))
            keys (sort (keys key-map))
            key-lengths (map count keys)
            max-key-length (apply max key-lengths)
            left-width (+ 2 max-key-length)]
        (doseq [key keys]
          (<< endl (indentation indent) (left-col key left-width))
          (make-pretty (get the-map (get key-map key)) indent)))
      (<< "}"))))

(defn- make-pretty
  ([thing] (make-pretty thing 0))
  ([thing indent]
    (cond
      (map? thing) (<<-map thing (inc indent))
      (nil? thing) (<< "nil")
      :else (<< thing))))

(defn pretty-map [value]
  (binding [*buffer* (StringBuffer.)]
    (make-pretty value)
    (.toString *buffer*)))
