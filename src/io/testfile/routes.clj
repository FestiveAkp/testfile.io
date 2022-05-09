(ns io.testfile.routes
  (:require [compojure.core :refer [defroutes GET ANY]]
            [compojure.route :as route]
            [compojure.coercions :refer [as-int]]
            [clojure.pprint :refer [pprint]]
            [cheshire.core :as cheshire]
            [io.testfile.faker :as faker]
            [io.testfile.response :as response]))

(defn text-file
  "Fetches a literary text file from the filesystem based on the size requested.
   - `size` is an optional query parameter, if `nil` the default value is `sm`.
   
   Returns an OK response map with the requested file as the body, otherwise 
   returns an error response if the query parameters are not valid."
  [size]
  (let [filepath "resources/text/"
        file (case (or size "sm")
               "sm" "ozymandias.txt"
               "md" "artofwar.txt"
               "lg" "odyssey.txt"
               "xl" "websters.txt"
               nil)]
    (if-not file
      (response/unprocessable-entity "Error: query parameter 'size' should be one of: [sm, md, lg, xl]")
      (-> (slurp (str filepath file))
          (response/ok)))))

(defn bee-movie-script
  []
  (-> (slurp "resources/text/beemovie.txt")
      (response/ok)))

(defn users-json-file
  "Generates a JSON file of random users based on the number of records requested.
   - `records` is an optional query parameter, if `nil` or not a number the default value is 3.
   
   Returns an OK response map with the generated JSON file as the body."
  [records]
  (let [count (or (as-int records) 3)]
    (-> (repeatedly count faker/generate-user)
        (cheshire/generate-string)
        (response/ok)
        (assoc :headers {"Content-Type" "application/json"}))))

(defroutes routes
  (GET "/"     []        (response/ok "hello, world!"))
  (ANY "/echo" request   (response/ok (with-out-str (pprint request))))
  (GET "/text" [size]    (text-file size))
  (GET "/bee"  []        (bee-movie-script))
  (GET "/json" [records] (users-json-file records))
  (route/not-found (response/not-found)))
