(ns joodo.kuzushi.commands.version-spec
  (:use
    [speclj.core]
    [joodo.kuzushi.commands.version]
    [joodo.kuzushi.spec-helper]
    [joodo.kuzushi.core :only (run)]
    [joodo.kuzushi.common :only (endl)]))

(describe "Version Comamnd"

  (with-command-help)

  (it "parses no args"
    (should= {} (parse-args)))

  (it "handles the --version command"
    (should= 0 (run "--version"))
    (should= (str "joodo/kuzushi " joodo.kuzushi.version/string endl) (to-s @output)))

  )

(run-specs)