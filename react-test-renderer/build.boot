(set-env!
  :resource-paths #{"resources"}
  :dependencies '[[cljsjs/boot-cljsjs "0.10.5" :scope "test"]
                  [cljsjs/react "16.13.0-0"]
                  [cljsjs/scheduler "0.19.0-0"]])

(require '[cljsjs.boot-cljsjs.packaging :refer :all])

(def +lib-version+ "16.13.0")
(def +version+ (str +lib-version+ "-1"))

(task-options!
 pom  {:project     'cljsjs/react-test-renderer
       :version     +version+
       :description "The React test renderer"
       :url         "http://facebook.github.io/react/"
       :scm         {:url "https://github.com/cljsjs/packages"}
       :license     {"MIT" "http://opensource.org/licenses/MIT"}})

(deftask package []
  (comp
    (download :url (format "https://unpkg.com/react-test-renderer@%s/umd/react-test-renderer.development.js" +lib-version+)
              :target "cljsjs/react-test-renderer/development/react-test-renderer.inc.js")
    (download :url (format "https://unpkg.com/react-test-renderer@%s/umd/react-test-renderer.production.min.js" +lib-version+)
              :target "cljsjs/react-test-renderer/production/react-test-renderer.min.inc.js")
    (deps-cljs :foreign-libs [{:file #"react-test-renderer.min.inc.js"
                               :file-min #"react-test-renderer.inc.js"
                               :requires ["react" "scheduler/unstable_mock" "scheduler"]
                               :provides ["react-test-renderer" "cljsjs.react.test-renderer"]
                               :global-exports {"react-test-renderer" "ReactTestRenderer"}}])
    (pom)
    (jar)
    (validate)))
