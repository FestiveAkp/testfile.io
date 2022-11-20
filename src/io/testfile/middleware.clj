(ns io.testfile.middleware
  (:require [clojure.tools.logging :as log]
            [ring.middleware.ratelimit :refer [wrap-ratelimit ip-limit]]
            [io.testfile.response :refer [too-many-requests]]))

(defn wrap-logging
  "Middleware for logging requests to the console."
  [handler]
  (fn [request]
    (let [response (handler request)]
      (log/info {:uri              (get request :uri)
                 :request-method   (get request :request-method)
                 :content-type     (get-in request [:headers "content-type"])
                 :content-encoding (get-in request [:headers "content-encoding"])
                 :query-string     (get request :query-string)
                 :http-status      (get response :status)
                 :user-agent       (get-in request [:headers "user-agent"])})
      response)))

(defn wrap-rate-limiting
  "Middleware for rate-limiting requests to the server."
  [handler]
  (-> handler
      (wrap-ratelimit {:limits [(ip-limit 1000)] ; 1000 req/hr/ip
                       :err-handler too-many-requests})))
