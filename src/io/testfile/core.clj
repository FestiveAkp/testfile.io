(ns io.testfile.core
  (:require [ring.adapter.jetty :as ring]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [io.testfile.routes :refer [routes]])
  (:gen-class))

(def app (wrap-defaults #'routes api-defaults))

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
    (start-server port)))
