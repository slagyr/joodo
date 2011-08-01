(ns joodo.spec-helpers.mail
  (:use
    [speclj.core]
    [joodo.mail])
  (:import
    [com.google.appengine.api.mail MailService]))

(deftype FakeMailService [sends sends-to-admins]
  MailService
  (send [this message] (swap! sends conj (message->map message)))
  (sendToAdmins [this message] (swap! sends-to-admins conj (message->map message))))

(def fake-mail-service (atom nil))

(defn with-fake-mail []
  (before
    (reset! fake-mail-service (FakeMailService. (atom []) (atom [])))
    (reset! mail-service-instance @fake-mail-service)))

(defn sent-messages []
  @(.sends @fake-mail-service))

(defn sent-messages-to-admins []
  @(.sends-to-admins @fake-mail-service))
