(set-env!
  :resource-paths #{"resources"}
  :dependencies '[[cljsjs/boot-cljsjs "0.10.5" :scope "test"]])

(require '[cljsjs.boot-cljsjs.packaging :refer :all])

(def +lib-version+ "5.0.3")
(def +version+ (str +lib-version+ "-0"))

(task-options!
 push {:ensure-clean false}
 pom  {:project     'cljsjs/decimal
       :version     +version+
       :description "A JavaScript library for arbitrary-precision decimal and non-decimal arithmetic."
       :url         "http://mikemcl.github.io/decimal.js"
       :license     {"MIT" "http://opensource.org/licenses/MIT"}
       :scm         {:url "https://github.com/cljsjs/packages"}})

(deftask package []
  (comp
   (download :url (format "https://github.com/MikeMcl/decimal.js/archive/v%s.zip" +lib-version+)
             :unzip true)
   (sift :move {#"^decimal.js-[^\/]*/decimal\.js"      "cljsjs/decimal/development/decimal.inc.js"
                #"^decimal.js-[^\/]*/decimal\.min\.js" "cljsjs/decimal/production/decimal.min.inc.js"})
   (sift :include #{#"^cljsjs"})
   (deps-cljs :name "cljsjs.decimal")
   (pom)
   (jar)
   (validate-checksums)))
