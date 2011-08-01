(ns joodo.kuzushi.core-spec
  (:use
    [speclj.core]
    [joodo.kuzushi.spec-helper]
    [joodo.kuzushi.core]
    [joodo.kuzushi.common :only (exit endl)]
    [joodo.kuzushi.commands.help :only (all-commands)])
  (:require
    [joodo.kuzushi.version]))


(describe "Gaeshi main"

  (with-command-help)

  (it "parses no arguments"
    (binding [all-commands (fn [& args] [])]
      (should= -1 (parse-args))))

  (it "parses command arg dirs"
    (should= "one" (:command (parse-args "one")))
    (should= "two" (:command (parse-args "two")))
    (should= ["two" "three"] (:*leftover (parse-args "one" "two" "three"))))

  (it "parses the --version switch"
    (should= nil (:version (parse-args "")))
    (should= "version" (:command (parse-args "--version")))
    (should= "version" (:command (parse-args "-v"))))

  (it "parses the --help switch"
    (should= nil (:comand (parse-args "")))
    (should= "help" (:command (parse-args "--help")))
    (should= "help" (:command (parse-args "-h"))))
  )

(run-specs)
