(ns io.testfile.response)

(def headers
  {"Content-Type" "text/plain"})

(defn ok [body] 
  {:status 200
   :body body
   :headers headers})

(defn not-found []
  {:status 404
   :body "404 Not Found"
   :headers headers})

(defn unprocessable-entity [message]
  {:status 422
   :body message
   :headers headers})
