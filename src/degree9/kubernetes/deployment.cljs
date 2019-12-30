(ns degree9.kubernetes.deployment
  (:require [degree9.debug :as dbg]))

(dbg/defdebug debug "degree9:enterprise:kubernetes:deployment")

;; Kubernetes Deployment ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn- list-deployment
  "List all Kubernetes deployments from a Kubernetes namespace ."
  [api namespace]
  (debug "Listing all Kubernetes deployments from namespace" api namespace)
  (-> api
    (.listNamespacedDeployment namespace)
    (.then k8s-response)
    (.catch k8s-error)))

(defn- read-deployment
  "Read a Deployment from a Kubernetes namespace."
  [api name namespace]
  (debug "Reading all Kubernetes deployments from namespace" api name namespace)
  (-> api
    (.readNamespacedDeployment name namespace)
    (.then k8s-response)
    (.catch k8s-error)))

(defn- create-deployment
  "Create a Kubernetes deployment within a Kubernetes namespace."
  [api data namespace]
  (debug "Creating all Kubernetes deployments from namespace" api data namespace)
  (-> api
    (.createNamespacedDeployment namespace data)
    (.then k8s-response)
    (.catch k8s-error)))

(defn- replace-deployment
  "Replace a Kubernetes deployment."
  [api id namespace data]
  (debug "Replacing all Kubernetes deployments from namespace" api id namespace data)
  (-> api
    (.replaceNamespacedDeployment id namespace data)
    (.then k8s-response)
    (.catch k8s-error)))

(defn- patch-deployment
  "Patch a Kubernetes deployment."
  [api id namespace data]
  (debug "Patching all Kubernetes deployments from namespace" api id namespace data)
  (-> api
    (.patchNamespacedDeployment id namespace data)
    (.then k8s-response)
    (.catch k8s-error)))

(defn- delete-deployment
  "Delete a Kubernetes deployment."
  [api id namespace]
  (debug "Deleting all Kubernetes deployments from namespace" api id namespace)
  (-> api
    (.deleteNamespacedDeployment id namespace)
    (.then k8s-response)
    (.catch k8s-error)))

(defn deployment [& [opts]]
  (let [api (:api opts)])
  (debug "Initializing all Kubernetes deployments from namespace" api
    (reify
      Object
      (find [this params]
        (let [namespace (get-in (js->clj params) ["query" "namespace"])]
          (list-deployment api namespace)))
      (get [this id params]
        (let [namespace (get-in (js->clj params) ["query" "namespace"])]
          (read-deployment api id namespace)))
      (create [this data & [params]]
        (let [namespace (get-in (js->clj params) ["query" "namespace"])]
          (create-deployment api data namespace)))
      (update [this id data params]
        (let [namespace (get-in (js->clj params) ["query" "namespace"])]
          (replace-deployment api id namespace data)))
      (patch [this id data params]
        (let [namespace (get-in (js->clj params) ["query" "namespace"])]
          (patch-deployment api id namespace data)))
      (remove [this id params]
        (let [namespace (get-in (js->clj params) ["query" "namespace"])]
          (delete-deployment api id namespace))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;