(ns joodo.main)

(defn -main [& args]
  (let [port (get (System/getenv) "PORT" "8080")
        env (or (first args) "development")]
    (joodo.kake.JoodoServer/main (into-array String ["-p" (str port) "-a" "127.0.0.1" "-e" env]))))
