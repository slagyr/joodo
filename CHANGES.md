# 2.0.0

* Joodo is a library, not a plugin
* Adds lein-template for joodo apps
* adds ring plugin to project.clj
* removes joodo/handler.clj and joodo/main.clj
* removes joodo/http package (was no longer used)
* removes lein-joodo plugin project
* moves pretty-map into chee
* renames env/development-env? to env/development? (same with production)
* updates dependencies compojure, ring-core, hiccup
* removes dependencies filecabinet, mmargs, ring-jetty-adapter
* removes controller/controller-router

# 1.2.2

* adds should-redirect-after-post to controller spec-helper

# 1.2.1

* [chee] Refactors coerce specs; adds support for most primative Java types (@mylesmegyesi): https://github.com/slagyr/joodo/pull/23

# 1.2.0

* Upgrades to Speclj 2.6.1
* Upgraded from Clojure 1.4.0 to 1.5.1
* generated project updated to work with lein2 on heroku cedar
* default bind address changed from 127.0.0.1 to 0.0.0.0

# 1.1.2

* caches template files when not in development mode
* locale middleware accepts a prioritized list of locale augmenter functions
* locale middleware allows locale to be pulled from the cookies

# 1.1.1

* clears rendered-context in controller spec-helper
* updates all dependencies to latest version

# 1.1.0

* updates to speclj 2.3.4
* adds file-info and head middlewares by default

# 1.0.0

* removes Servlets and all Java code
* updates all dependencies
* updates generated code
* uses ring middleware instead of builtin middleware

# 0.12.0

* added a default middleware to avoid favicon requests to the app server

# 0.11.0

* spec-helpers.view/with-mock-render accepts a :strict option to fail when templates don't exist
* *env* changes to regular var instead of atom.  Add alter-env! and set-env! to joodo.env.
* configuration loading moved to joodo.env.
* configuration :joodo.core.namespace renamed to :joodo.root.namespace
* changes default app.core to app.root for generated projects

# 0.10.0

* Upgraded to support leiningen 2 as well as leiningen 1.

# 0.9.0

* Upgraded from Clojure 1.2.* to 1.4.0
