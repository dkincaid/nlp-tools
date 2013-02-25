(ns nlp-tools.similarity
  (:require [clojure.set])
  (:import [org.apache.lucene.search.spell JaroWinklerDistance LevensteinDistance NGramDistance]))

(defn- mk-distance-fn
  [dist-obj]
  (fn [string1 string2] (.getDistance dist-obj string1 string2)))

(defn jaro-winkler-distance
  "Calculates the Jaro-Winkler distance between the strings."
  [string1 string2]
  ((mk-distance-fn (JaroWinklerDistance.)) string1 string2))

(defn levenstein-distance
  "Calculates the Levenstein distance between the strings."
  [string1 string2]
  ((mk-distance-fn (LevensteinDistance.)) string1 string2))

(defn ngram-distance
  "Calculates the n-gram distance between the two strings."
  [string1 string2 n]
  ((mk-distance-fn (NGramDistance. n)) string1 string2))

(defn jaccard-distance
  "Calculates the Jaccard distance between two strings."
  [string1 string2]
  (let [set1 (set (seq string1))
        set2 (set (seq string2))]
    (float (/ (count (clojure.set/intersection set1 set2))
              (count (clojure.set/union set1 set2))))))


