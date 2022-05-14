(ns io.testfile.util
  (:require [compojure.coercions :refer [as-int]]
            [clojure.string :refer [upper-case]]))

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
    (try
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
            nil)))
      (catch Exception _ nil))))
