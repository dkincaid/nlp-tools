(defproject nlp-tools "0.0.1-SNAPSHOT"
  :description "A set of Clojure NLP tools made easy to use."
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.apache.lucene/lucene-core "3.5.0"]
                 [org.apache.lucene/lucene-spellchecker "3.5.0"]
                 [org.apache.opennlp/opennlp-tools "1.5.2-incubating"]
                 [edu.stanford.nlp/stanford-corenlp "1.3.4"]
                 [edu.stanford.nlp/stanford-corenlp "1.3.4" :classifier "models"]]
  :profiles { :dev { :dependencies [[midje "1.5-beta2"]]}})
