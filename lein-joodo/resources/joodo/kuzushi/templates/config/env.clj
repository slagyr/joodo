(use 'joodo.env)

(def environment {
  :joodo-env "!-ENV-!"
  })

(swap! *env* merge environment)