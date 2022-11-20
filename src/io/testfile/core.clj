(ns io.testfile.core
  (:require [ring.adapter.jetty :as ring]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [clojure.tools.logging :as log]
            [io.testfile.routes :refer [routes]]
            [io.testfile.middleware :refer [wrap-logging wrap-rate-limiting]])
  (:gen-class))

(def app (-> #'routes
             wrap-rate-limiting
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
