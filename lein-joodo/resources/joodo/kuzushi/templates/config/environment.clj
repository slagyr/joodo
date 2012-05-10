(use 'joodo.env)

(def environment {
  :chee.util.namespace "!-APP_NAME-!.core"
  ; environment settings go here
  })

(swap! *env* merge environment)