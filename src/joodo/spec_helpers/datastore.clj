(ns joodo.spec-helpers.datastore
  (:use
    [speclj.core])
  (:import
    [com.google.appengine.tools.development.testing
     LocalServiceTestConfig
     LocalDatastoreServiceTestConfig
     LocalBlobstoreServiceTestConfig
     LocalServiceTestHelper]
    [com.google.apphosting.api ApiProxy]))

(defn tear-down []
  (.stop (ApiProxy/getDelegate))
  (ApiProxy/clearEnvironmentForCurrentThread)
  )

(defn with-local-datastore []
  (around [it]
    (try
      (.setUp (LocalServiceTestHelper.
        (into-array LocalServiceTestConfig [(LocalBlobstoreServiceTestConfig.) (LocalDatastoreServiceTestConfig.)])))
      (it)
      (finally (tear-down)))))
