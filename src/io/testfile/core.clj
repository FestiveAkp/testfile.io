(ns io.testfile.core
  (:require [ring.adapter.jetty :as ring]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [io.testfile.routes :refer [routes]]
            [clojure.tools.logging :as log])
  (:gen-class))

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

(def app (-> #'routes
             wrap-logging
             (wrap-defaults api-defaults)))

(defonce server (atom nil))

(defn start-server [port]
  (reset! server
          (ring/run-jetty (fn [req] (app req)) {:port port :join? false})))

(defn stop-server []
  (when-some [s @server]
    (.stop s)
    (reset! server nil)))

(defn -main []
  (let [port (Integer. (or (System/getenv "PORT") "8080"))] 
    (start-server port)
    (log/info "Server initialized on port" port)))
