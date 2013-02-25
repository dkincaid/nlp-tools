(ns nlp-tools.similarity
    (:require [clojure.set])
  (:import org.apache.lucene.search.spell.JaroWinklerDistance
           org.apache.lucene.search.spell.LevensteinDistance
           org.apache.lucene.search.spell.NGramDistance))

(defn jaro-winkler-distance [string1 string2]
  (.getDistance (JaroWinklerDistance.) string1 string2))

(defn levenstein-distance [string1 string2]
  (.getDistance (LevensteinDistance.) string1 string2))

(defn ngram-distance [string1 string2 n]
  (.getDistance (NGramDistance. n) string1 string2))

(defn jaccard-distance [string1 string2]
  (let [set1 (set (seq string1))
        set2 (set (seq string2))]
    (float (/ (count (clojure.set/intersection set1 set2))
              (count (clojure.set/union set1 set2))))))


