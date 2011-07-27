(ns joodo.tsukuri.joodo-dev-server-spec
  (:use
    [speclj.core])
  (:import
    [joodo.tsukuri GaeshiDevServer]))

(describe "Gaeshi Dev Server"
  (with server (GaeshiDevServer.))
  (with env (.getEnv @server))

  (it "handles default options"
    (.parseArgs @server (into-array String []))
    (should= "development" (.env @env))
    (should= "127.0.0.1" (.address @env))
    (should= 8080 (.port @env))
    (should= "." (.dir @env)))

  (it "optionaly changes the port"
    (.parseArgs @server (into-array String ["-p" "1234"]))
    (should= 1234 (.port @env))
    (.parseArgs @server (into-array String ["--port=4321"]))
    (should= 4321 (.port @env)))

  (it "optionaly changes the address"
    (.parseArgs @server (into-array String ["-a" "1.2.3.4"]))
    (should= "1.2.3.4" (.address @env))
    (.parseArgs @server (into-array String ["--address=4.3.2.1"]))
    (should= "4.3.2.1" (.address @env)))

  (it "optionaly changes the env"
    (.parseArgs @server (into-array String ["-e" "foo"]))
    (should= "foo" (.env @env))
    (.parseArgs @server (into-array String ["--environment=bar"]))
    (should= "bar" (.env @env)))

  (it "optionaly changes the dir"
    (.parseArgs @server (into-array String ["-d" "fizz"]))
    (should= "fizz" (.dir @env))
    (.parseArgs @server (into-array String ["--directory=bang"]))
    (should= "bang" (.dir @env)))

  )

(run-specs)