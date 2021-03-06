(ns degree9.pandadoc.core
  (:refer-clojure :exclude [get])
  (:require [goog.crypt.base64 :as base64]
            [clojure.string :as cstr]
            [degree9.env :as env]
            [degree9.request :as req]))

(defn- json->clj [res]
  (js->clj (.json res) :keywordize-keys true))

(defn- check-status [res]
  (if (.-ok res) res
    (throw (js/Error. (.-statusText res)))))

(defn- pandadoc-url [path & [query]]
  (str (env/get "PANDADOC" "https://api.pandadoc.com/public/v1/") path))

(defn- pandadoc-headers [headers]
  (let [username (env/get "PANDADOC")
        password (env/get "PANDADOC")
        auth     (base64/encodeString (str username ":" password))]
    (merge {:authorization (str "Basic " auth)} headers)))

(defn- pandadoc-request [{:keys [method path data query headers] :as opts}]
  (let [method  (cstr/upper-case (name method))
        headers (pandadoc-headers headers)
        url     (pandadoc-url path query)]
    (-> (req/fetch url (clj->js {:method method :body data :headers headers}))
        (.then check-status)
        (.then json->clj))))

(defn post [path data & [{:keys [headers] :as opts}]]
  (pandadoc-request
    {:method :post
     :path path
     :data data
     :headers headers}))

(defn get [path & [{:keys [headers] :as opts}]]
  (pandadoc-request
    {:method :get
     :path path
     :headers headers}))

(defn delete [path & [{:keys [headers] :as opts}]]
  (pandadoc-request
    {:method :delete
     :path path
     :headers headers}))

(defn put [path data & [{:keys [headers] :as opts}]]
  (pandadoc-request
    {:method :put
     :path path
     :data data
     :headers headers}))

(defn patch [path data & [{:keys [headers] :as opts}]]
  (pandadoc-request
    {:method :patch
     :path path
     :data data
     :headers headers}))
