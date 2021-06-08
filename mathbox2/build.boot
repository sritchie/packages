(set-env!
 :resource-paths #{"resources"}
 :dependencies '[[cljsjs/boot-cljsjs "0.10.5" :scope "test"]])

(require '[cljsjs.boot-cljsjs.packaging :refer :all])

(def +lib-version+ "0.0.5") ; MathBox 2, *not* MathBox 1
(def +version+ (str +lib-version+ "-0"))

(task-options!
 pom {:project 'cljsjs/mathbox2
      :version +version+
      :description "MathBox 2: Three-dimensional, animated mathematical visualization"
      :url "https://gitgud.io/unconed/mathbox"
      :license {"MIT" "http://opensource.org/licenses/MIT"}
      :scm {:url "https://github.com/cljsjs/packages"}})

(deftask build-mathbox []
  (run-commands :commands [["npm" "install" "--include-dev"]
                           ["npm" "run" "copy"]
                           ["npm" "run" "generate-extern"]
                           ["rm" "-rf" "./node_modules"]]))

;; Download URLs are pinned to MathBox commit
;; 92ae43e320ea9ac26ccbac5c4dfe6d32ec064d33; to ensure no possible breakage,
;; since versioning is quite informal for MathBox 1. TODO note that we're pinning to what I published.
;;
;; TODO update readme too!
(deftask package []
  (comp
   (build-mathbox)
   (sift :move {#".*mathbox2.bundle.js" "cljsjs/mathbox2/development/mathbox2.inc.js"
                #".*mathbox2.bundle.min.js" "cljsjs/mathbox2/production/mathbox2.min.inc.js"
                #".*mathbox2.ext.js" "cljsjs/mathbox2/common/mathbox2.ext.js"})
   (sift :include #{#"^cljsjs"})
   (deps-cljs
    :foreign-libs
    [{:file #"mathbox2.inc.js"
      :file-min #"mathbox2.min.inc.js"
      :provides ["@sicmutils/mathbox2", "cljsjs.mathbox2"]
      :requires []
      :global-exports '{"@sicmutils/mathbox2" MathBox
                        cljsjs.mathbox2 MathBox}}])
   (pom)
   (jar)
   (validate-checksums)))
