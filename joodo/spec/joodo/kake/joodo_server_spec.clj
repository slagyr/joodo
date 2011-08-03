(ns joodo.kake.joodo_server_spec
  (:use
    [speclj.core])
  (:import
    [joodo.kake JoodoServer]))

(describe "Joodo Server"
  (with server (JoodoServer.))
  (with env (.getEnv @server))

  (it "handles default options"
    (.parseArgs @server (into-array String []))
    (should= "development" (.env @server))
    (should= "127.0.0.1" (.address @server))
    (should= 8080 (.port @server))
    (should= "." (.dir @server)))

  (it "optionaly changes the port"
    (.parseArgs @server (into-array String ["-p" "1234"]))
    (should= 1234 (.port @server))
    (.parseArgs @server (into-array String ["--port=4321"]))
    (should= 4321 (.port @server)))

  (it "optionaly changes the address"
    (.parseArgs @server (into-array String ["-a" "1.2.3.4"]))
    (should= "1.2.3.4" (.address @server))
    (.parseArgs @server (into-array String ["--address=4.3.2.1"]))
    (should= "4.3.2.1" (.address @server)))

  (it "optionaly changes the env"
    (.parseArgs @server (into-array String ["-e" "foo"]))
    (should= "foo" (.env @server))
    (.parseArgs @server (into-array String ["--environment=bar"]))
    (should= "bar" (.env @server)))

  (it "optionaly changes the dir"
    (.parseArgs @server (into-array String ["-d" "fizz"]))
    (should= "fizz" (.dir @server))
    (.parseArgs @server (into-array String ["--directory=bang"]))
    (should= "bang" (.dir @server)))

  (it "loads the joodo-env"
    (.parseArgs @server (into-array String ["-e" "staging"]))
    (should= "staging" (System/getProperty "joodo-env"))
    (.parseArgs @server (into-array String ["--environment" "test"]))
    (should= "test" (System/getProperty "joodo-env")))
  )

(run-specs)