(ns io.testfile.response
  (:require [ring.util.response :as ring]))

(defn ok [body]
  (-> (ring/response body)
      (ring/content-type "text/plain")))

(defn json [body]
  (-> (ring/response body)
      (ring/content-type "application/json")))

(defn text-resource [path]
  (-> (ring/resource-response path)
      (ring/content-type "text/plain")))

(defn not-found []
  (-> (ring/response "404 Not Found")
      (ring/status 404)
      (ring/content-type "text/plain")))

(defn unprocessable-entity [message]
  (-> (ring/response message)
      (ring/status 422)
      (ring/content-type "text/plain")))
