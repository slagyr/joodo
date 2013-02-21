(ns sample.main)

(defn -main []
  (let [port (Integer/parseInt (get (System/getenv) "PORT" "8080"))]
    (joodo.tsukuri.GaeshiDevServer/main (into-array String ["-p" (str port) "-a" "0.0.0.0"]))))


