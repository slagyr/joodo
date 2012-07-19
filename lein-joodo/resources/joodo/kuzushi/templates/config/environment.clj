(use 'joodo.env)

(def environment {
  :joodo.core.namespace "!-APP_NAME-!.core"
  ; environment settings go here
  })

(swap! *env* merge environment)