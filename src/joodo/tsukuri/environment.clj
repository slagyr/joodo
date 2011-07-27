(ns joodo.tsukuri.environment
  (:import
    [joodo.tsukuri GaeshiDevServerEnvironment GaeshiApiProxyEnvironment]))

(defn setup-environment [app]
  (.install (GaeshiDevServerEnvironment.))
  (.install (GaeshiApiProxyEnvironment. app)))
