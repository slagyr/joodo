# ?

* adds lein-joodo plugin to generated project.clj

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
