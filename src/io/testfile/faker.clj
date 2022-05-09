(ns io.testfile.faker
  (:import (com.github.javafaker Faker)))

(defn first-name
  "Generate a first name using Faker."
  []
  (-> (Faker.)
      (.name)
      (.firstName)))

(defn last-name
  "Generate a last name using Faker."
  []
  (-> (Faker.)
      (.name)
      (.lastName)))

(defn full-address
  "Generate a full address using Faker."
  []
  (-> (Faker.)
      (.address)
      (.fullAddress)))

(defn university-name
  "Generate a university name using Faker."
  []
  (-> (Faker.)
      (.university)
      (.name)))

(defn birthday
  "Generate a birthdate using Faker."
  []
  (-> (Faker.)
      (.date)
      (.birthday)))

(defn color-name
  "Generate a color using Faker."
  []
  (-> (Faker.)
      (.color)
      (.name)))

(defn music-genre
  "Generate a genre using Faker."
  []
  (-> (Faker.)
      (.music)
      (.genre)))

(defn cell-phone-number
  "Generate a cell phone number using Faker."
  []
  (-> (Faker.)
      (.phoneNumber)
      (.cellPhone)))

(defn job-title
  "Generate a job title using Faker."
  []
  (-> (Faker.)
      (.job)
      (.title)))

(defn email
  "Generate an email using Faker."
  []
  (-> (Faker.)
      (.internet)
      (.safeEmailAddress)))

(defn generate-user
  "Generate a fake user data object."
  []
  {:firstName (first-name)
   :lastName (last-name)
   :phone (cell-phone-number)
   :address (full-address)
   :email (email)
   :birthday (birthday)
   :favoriteColor (color-name)
   :favoriteGenre (music-genre)
   :university (university-name)
   :jobTitle (job-title)})
