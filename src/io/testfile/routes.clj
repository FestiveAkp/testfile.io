(ns io.testfile.routes
  (:require [compojure.core :refer [defroutes GET ANY]]
            [compojure.route :as route]
            [compojure.coercions :refer [as-int]]
            [clojure.pprint :refer [pprint]]
            [cheshire.core :as cheshire]
            [io.testfile.faker :as faker]
            [io.testfile.response :as response]
            [io.testfile.util :refer [human-filesize-to-bytes]]))

(defn welcome-file
  "Returns the welcome text for the application."
  []
  (response/text-resource "text/welcome.txt"))

(defn text-file
  "Fetches a literary text file from the filesystem based on the size requested.
   - `size` is a route parameter, should be one of [sm, md, lg, xl]."
  [size]
  (let [file (case size
               "sm" "ozymandias.txt"
               "md" "artofwar.txt"
               "lg" "odyssey.txt"
               "xl" "websters.txt"
               nil)]
    (if-not file
      (response/not-found)
      (response/text-resource (str "text/" file)))))

(defn random-file
  "Generates a file filled with random alphanumeric characters based on the size requested.
   - `size` is a route parameter specifying a 'human-readable' filesize, like 50KB."
  [size]
  (let [characters "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"
        GB 1073741824
        size-bytes (human-filesize-to-bytes size)]
    (cond
      (nil? size-bytes) (response/not-found) ; could not parse input -> cannot find file -> 404
      (> size-bytes (* 100 GB)) (response/unprocessable-entity "Error: requested too many bytes (> 100GB).")
      :else (repeatedly size-bytes #(rand-nth characters)))))

(defn utf-8-test-file
  "Returns Markus Kuhn's UTF-8 encoded sample plain-text file."
  []
  (response/text-resource "text/utf-8.txt"))

(defn language-test-file
  "Returns the 'I Can Eat Glass' section of The Kermit Project's UTF-8 Sampler."
  []
  (response/text-resource "text/languages.txt"))

(defn pi-file
  "Returns a text file containing N digits of pi."
  [digits]
  (let [digits (or (as-int digits) 2)
        end (+ digits 2)]
    (if (> digits 100000)
      (response/unprocessable-entity "Error: requested too many digits (> 100,000).")
      (-> (slurp "resources/text/pi.txt")
          (subs 0 end)
          (response/ok)))))

(defn users-json-file
  "Generates a JSON file of random users based on the number of records requested.
   - `records` is an optional query parameter, if `nil` or not a number the default value is 3."
  [records]
  (let [count (or (as-int records) 3)]
    (-> (repeatedly count faker/generate-user)
        (cheshire/generate-string)
        (response/json))))

(defn bee-movie-script
  "Returns the entire Bee Movie script."
  []
  (response/text-resource "text/beemovie.txt"))

(defn favicon
  "Returns a typical 16x16 favicon used on the web."
  []
  (response/favicon "images/favicon.ico"))

(defroutes routes
  (GET "/"            [] (welcome-file))
  (ANY "/echo"        request (response/ok (with-out-str (pprint request))))
  (GET "/text"        [] (text-file "sm"))
  (GET "/text/:size"  [size] (text-file size))
  (GET "/utf-8"       [] (utf-8-test-file))
  (GET "/languages"   [] (language-test-file))
  (GET "/pi"          [digits] (pi-file digits))
  (GET "/json"        [records] (users-json-file records))
  (GET "/beemovie"    [] (bee-movie-script))
  (GET "/favicon"     [] (favicon))
  (GET "/:size"       [size] (random-file size))
  (route/not-found    (response/not-found)))
