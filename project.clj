(defproject nlp-tools "0.0.1-SNAPSHOT"
  :description "A set of Clojure NLP tools made easy to use."
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "same as Clojure"}
  :aot [nlp-tools.anafora.corpus]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.apache.lucene/lucene-core "6.0.1"]
                 [org.apache.lucene/lucene-analyzers-common "6.0.1"]
                 [org.apache.lucene/lucene-spellchecker "3.6.2"]
                 [org.apache.opennlp/opennlp-tools "1.6.0"]
                 [edu.stanford.nlp/stanford-corenlp "3.6.0"]
                 [edu.stanford.nlp/stanford-corenlp "3.6.0" :classifier "models"]
                 [com.damballa/abracad "0.4.13"]
                 [com.taoensso/timbre "4.3.1"]
                 [org.clojure/tools.cli "0.3.5"]]
  :profiles { :dev { :dependencies [[midje "1.8.3"]]}}
)
