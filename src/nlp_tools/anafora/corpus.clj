(ns nlp-tools.anafora.corpus
  (:require [abracad.avro :as avro]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [taoensso.timbre :as timbre]
            [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(timbre/refer-timbre)

(defn write-doc
  "Write the given document into an Anafora compatible corpus"
  [corpus-dir doc-id doc-text]
  (info "Converting" doc-id)
  (io/make-parents (str/join "/" [corpus-dir doc-id doc-id]))
  (with-open [w (io/writer (str/join "/" [corpus-dir doc-id doc-id]))]
    (.write w doc-text)))

(defn avro->corpus
  "Convert all the documents found in the given Avro file into an Anafora corpus at the given directory."
  [doc-id-field text-field avro-file destination]
  (with-open [adf (avro/data-file-reader avro-file)]
    (doseq [d adf]
      (write-doc destination (get d doc-id-field)
                   (get d text-field)) adf)))

(def cli-options
  [["-a" "--avro-file FILE" "Avro file name to read from"]
   ["-c" "--corpus-dir DIR" "Corpus directory to write into"]
   ["-k" "--id-field FIELD" "Document id field in the Avro schema"]
   ["-t" "--text-field FIELD" "Text field in the Avro schema"]
   ["-h" "--help"]])

(defn usage [options-summary]
  (->> ["Avro to Anafora corpus converter."
        ""
        "Usage: corpus [options]"
        ""
        "Options:"
        options-summary]
       (str/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (str/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn -main
  [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options) (exit 0 (usage summary))
      errors (exit 1 (error-msg errors)))
    (avro->corpus (keyword (:id-field options))
                  (keyword (:text-field options))
                  (:avro-file options)
                  (:corpus-dir options))))
