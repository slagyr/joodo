(ns joodo.cmd
  (:import
    [java.io BufferedInputStream ByteArrayInputStream]))

(defn- copy-bytes [in out]
  (with-open [input in]
    (loop [b (.read input)]
      (if (not (= -1 b))
        (do
          (.write out b)
          (.flush out)
          (recur (.read input)))
        (do
          (.close in)
          (.close out))))))

(defn exec
  ([command-parts] (exec command-parts ""))
  ([command-parts input]
    (let [command-ary (into-array command-parts)
          process (.exec (Runtime/getRuntime) command-ary)
          input (ByteArrayInputStream. (.getBytes input))
          input-thread (Thread. #(copy-bytes input (.getOutputStream process)))
          output (BufferedInputStream. (.getInputStream process))
          output-thread (Thread. #(copy-bytes output System/out))
          error (BufferedInputStream. (.getErrorStream process))
          error-thread (Thread. #(copy-bytes error System/err))]
      (.start input-thread)
      (.start output-thread)
      (.start error-thread)
      (.waitFor process)
      (.join output-thread 1000)
      (.join error-thread 1000)
      (.exitValue process))))

(defn java [jvm-args main-class args]
  (let [java-exe (str (System/getProperty "java.home") "/bin/java")
        command-parts (concat [java-exe] jvm-args [main-class] args)]
    (exec command-parts)))
