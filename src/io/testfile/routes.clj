(ns io.testfile.routes
  (:require [compojure.core :refer [defroutes GET ANY]]
            [compojure.route :as route]
            [compojure.coercions :refer [as-int]]
            [clojure.pprint :refer [pprint]]
            [clojure.string :refer [upper-case]]
            [cheshire.core :as cheshire]
            [io.testfile.faker :as faker]
            [io.testfile.response :as response]))

(defn text-file
  "Fetches a literary text file from the filesystem based on the size requested.
   - `size` is an optional query parameter, if `nil` the default value is `sm`."
  [size]
  (let [file (case (or size "sm")
               "sm" "ozymandias.txt"
               "md" "artofwar.txt"
               "lg" "odyssey.txt"
               "xl" "websters.txt"
               nil)]
    (if-not file
      (response/unprocessable-entity "Error: query parameter 'size' should be one of: [sm, md, lg, xl]")
      (response/text-resource (str "text/" file)))))

(defn human-filesize-to-bytes
  "Converts a human readable file size to its equivalent representation in bytes.
   
   Examples:
   - 2MB = 2097152
   - 42KB = 43008
   - 10 = 10
   - 1B = 1"
  [human-string]
  (if (number? (as-int human-string))
    (as-int human-string) ; pass through if just an integer
    (let [re-match (re-find #"([\d.,]+)\s*(\w+)" human-string)
          magnitude (some-> re-match
                            (get 1)
                            (Double/parseDouble))
          unit (some-> re-match
                       (get 2)
                       (upper-case))]
      (if (nil? magnitude)
        nil
        (case unit
          "GB" (Math/round (* magnitude 1073741824))
          "MB" (Math/round (* magnitude 1048576))
          "KB" (Math/round (* magnitude 1024))
          "B" (Math/round magnitude)
          nil)))))

(defn random-file
  "Generates a file filled with random alphanumeric characters based on the size requested.
   - `size` is an optional query parameter specifying a 'human-readable' filesize, 
     like 50KB. If not specified the size defaults to 1KB."
  [size]
  (let [characters "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"
        size-bytes (human-filesize-to-bytes (or size "1KB"))]
    (cond
      (nil? size-bytes)         (response/unprocessable-entity "Error: could not parse query parameter `size`.")
      (> size-bytes 1073741824) (response/unprocessable-entity "Error: requested too many bytes (> 1GB).")
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

(defroutes routes
  (GET "/"          [] (response/ok "hello, world!"))
  (ANY "/echo"      request (response/ok (with-out-str (pprint request))))
  (GET "/literary"  [size] (text-file size))
  (GET "/random"    [size] (random-file size))
  (GET "/utf-8"     [] (utf-8-test-file))
  (GET "/lang"      [] (language-test-file))
  (GET "/pi"        [digits] (pi-file digits))
  (GET "/json"      [records] (users-json-file records))
  (GET "/beemovie"  [] (bee-movie-script))
  (route/not-found  (response/not-found)))
