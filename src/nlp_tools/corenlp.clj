(ns nlp-tools.corenlp
  "The functions in this namespace wrap the Stanford coreNLP Java library. Start by calling 
the function (annotate) with a string holding the text that you want annotated. That function
returns an Annotation object. You can get the sentences from the annotation object using the 
(sentences) function. Those sentences can then be used with the other functions to get the various
annotations that were applied to your text."

  (:require [clojure.java.io :as io])
  (:import [java.util Properties]
   [edu.stanford.nlp.pipeline StanfordCoreNLP Annotation XMLOutputter]
   [edu.stanford.nlp.ling CoreAnnotations$SentencesAnnotation CoreAnnotations$TokensAnnotation
    CoreAnnotations$TextAnnotation CoreAnnotations$PartOfSpeechAnnotation
    CoreAnnotations$NamedEntityTagAnnotation]
   [edu.stanford.nlp.trees TreeCoreAnnotations$TreeAnnotation]
   [edu.stanford.nlp.trees.semgraph SemanticGraphCoreAnnotations$CollapsedCCProcessedDependenciesAnnotation]
   [edu.stanford.nlp.dcoref CorefCoreAnnotations$CorefChainAnnotation]))

(def slash-join
  "Join the strings in the collection with a slash."
  (partial clojure.string/join "/"))

(defn slash-cat
  "Join the given strings using a slash."
  [s1 s2 & s]
  (if (empty? s)
    (slash-join (list s1 s2))
    (slash-join (concat (list s1 s2) s))))

(defn pipeline
  "Create a StanfordCoreNLP pipeline."
  ([] (pipeline (doto (Properties.) 
                           (.put "annotators" "tokenize, ssplit, pos, lemma, ner, parse, dcoref"))))
  ([properties] (StanfordCoreNLP. properties)))

(defn annotate
  "Annotate the given text"
  ([text properties] (let [pipe (pipeline properties)
                            document (Annotation. text)]
                        (.annotate pipe document)
                        document))

  ([text] (annotate text (doto (Properties.) 
                           (.put "annotators" "tokenize, ssplit, pos, lemma, ner, parse, dcoref")))))

(defn sentences
  "Get the sentences out of the given annotated document. Returns a list of CoreMap's."
  [document]
  (.get document CoreAnnotations$SentencesAnnotation))

(defn- mk-tag-fn
  "Creates a function which gets a given annotation from a given object."
  [annotation]
  (fn [obj] (.get obj annotation)))

(def tokens
  "Gets the tokens from the sentence CoreMap. Returns a list of CoreLabel's"
  (mk-tag-fn CoreAnnotations$TokensAnnotation))

(def token-text
  "Gets the text of the token from the token CoreLabel."
  (mk-tag-fn CoreAnnotations$TextAnnotation))

(def token-pos
  "Gets the part of speech tag for the given token CoreLabel."
  (mk-tag-fn CoreAnnotations$PartOfSpeechAnnotation))

(def token-ner
  "Gets the named entity tag for the given token CoreLabel."
  (mk-tag-fn CoreAnnotations$NamedEntityTagAnnotation))

(def parse-tree
  "Gets the parse tree of the given sentence. Returns a edu.stanford.nlp.trees.Tree"
  (mk-tag-fn TreeCoreAnnotations$TreeAnnotation))

(def dependencies
  "Gets the dependency graph of the given sentence. Returns a edu.stanford.nlp.trees.semgraph.SemanticGraph"
  (mk-tag-fn SemanticGraphCoreAnnotations$CollapsedCCProcessedDependenciesAnnotation))

(def coref
  "Gets the coreference link graph of the given annotated document. Returns a map of integers to CorefChain."
  (mk-tag-fn CorefCoreAnnotations$CorefChainAnnotation))

(defn tag-tokens
  "Concatenates tags to the tokens in the given sentence using the functions provided. Returns
a seq containing each word in the text with the tags produced by the given tag functions concatenated
with a forward slash. (e.g. (tag-tokens my-sentence token-pos token-ner) => \"This/DT/O\")."
  [sentence tag-fn & fcns]
  (let [ts (tokens sentence)
        text (map token-text ts)]
    (apply (partial map slash-cat text)
           (for [fun (cons tag-fn fcns)]
             (map fun ts)))))

(defn xml-print!
  "Print the annotated text to the given xml file."
  [annotation filename]
  (let [pipe (pipeline)
        out (io/output-stream filename)]
    (XMLOutputter/xmlPrint annotation out pipe)))
