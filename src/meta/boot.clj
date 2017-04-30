(ns meta.boot
  {:boot/export-tasks true}
  (:require [boot.core :as boot]
            [boot.pod :as pod]
            [boot.file :as file]
            [boot.util :as util]
            [boot.task.built-in :as task]
            [meta.boot.util :as mutil]
            [meta.boot.impl :as impl]
            [clojure.java.io :as io]
            [clojure.string :as s]
            [adzerk.boot-cljs :as cljs]
            [degree9.boot-nodejs :as nj]
            [degree9.boot-semgit :as sg]
            [degree9.boot-semgit.workflow :as wf]
            [feathers.boot-feathers :as fs]
            [hoplon.boot-hoplon :as bh]))

;; Meta Boot ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; Meta Boot Tasks ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(boot/deftask initialize
  "Initialize [meta]."
  []
  (impl/init-impl)
  identity)

(def init initialize)

(boot/deftask proto
  "Configure [meta] for Proto-REPL."
  []
  (impl/proto-impl)
  identity)

(boot/deftask develop
  "Build project for local development."
  []
  (comp (sg/git-pull :branch "origin/master")
        (fs/feathers)
        (task/watch)
        (bh/hoplon)
        (nj/nodejs)
        (cljs/cljs)
        (task/target)))

(def dev develop)

(boot/deftask deploy
  "Deploy project to clojars."
  []
  identity)

(boot/deftask circle
  "Preload dependencies for Circle CI."
  []
  (util/info "Hello Circle CI!")
  identity)

(boot/deftask test
  "Run project tests."
  []
  (util/info "Running Tests...")
  identity)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Meta Boot Public API ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;(boot/deftask manifest
;  "Write a DSL manifest file."
;  [d dsl      VAL str    "DSL name."
;   e ext      VAL str    "DSL file extension."
;   r refers   VAL #{sym} "Set of namespaces to include in DSL files."]
;  (assert (:dsl *opts*) "A DSL name is required.")
;  (assert (:ext *opts*) "A DSL file extension is required.")
;  (boot/with-pre-wrap fileset
;    (let [tmp     (boot/tmp-dir!)
;          dslname (:dsl *opts*)
;          ext     (:ext *opts*)
;          out     (io/file tmp dslname "manifest.edn")
;          fspath->jarpath #(->> % file/split-path (s/join "/") boot/tmp-path)]
;      (when-let [dsl (->> fileset
;                          boot/output-files
;                          (boot/by-ext [ext])
;                          (map fspath->jarpath))]
;        (util/info "Writing DSL manifest...\n")
;        (doseq [d dsl] (util/info "• %s \n" d))
;        (spit (doto out io/make-parents) (pr-str (vec dsl))))
;      (-> fileset (boot/add-resource tmp) boot/commit!))))
