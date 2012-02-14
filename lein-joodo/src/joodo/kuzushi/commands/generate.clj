(ns joodo.kuzushi.commands.generate
  (:use
    [joodo.kuzushi.common :only (symbolize)]
    [joodo.kuzushi.generation :only (create-templater add-tokens ->path ->name)]
    [joodo.kuzushi.commands.help :only (usage-for)])
  (:import
    [filecabinet FileSystem Templater]
    [mmargs Arguments]))

(def arg-spec (Arguments.))
(doto arg-spec
  (.addParameter "generator" "The generator [controller]")
  (.addParameter "ns" "The namespace of the generated thing")
  (.addSwitchOption "f" "force" "Overwrite existing files"))

(defn parse-args [& args]
  (symbolize (.parse arg-spec (into-array String args))))

(defn generate-controller [templater options]
  (let [controller-ns (:ns options)
        controller-ns (if (.endsWith controller-ns "-controller") controller-ns (str controller-ns "-controller"))
        controller-path (->path controller-ns)
        controller-name (->name controller-ns)
        controller-subject (.replace controller-name "-controller" "")]
    (add-tokens templater "CONTROLLER_NS" controller-ns)
    (add-tokens templater "CONTROLLER_NAME" controller-name)
    (add-tokens templater "CONTROLLER_SUBJECT" controller-subject)
    (.file templater (format "spec/%s_spec.clj" controller-path) "spec/app/controller_spec.clj")
    (.file templater (format "src/%s.clj" controller-path) "src/app/controller.clj")))

(defn execute
  "Generates files for various components at the specified namespace:
    controller - new controller and spec file"
  [options]
  (let [templater (create-templater options)
        generator (.toLowerCase (:generator options))]
    (cond
      (= "controller" generator) (generate-controller templater options)
      :else (usage-for "generate" [(str "Unknown generator: " generator)]))))

