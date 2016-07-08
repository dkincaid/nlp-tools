(ns nlp-tools.tokenization
  (:import org.apache.lucene.analysis.standard.StandardAnalyzer
           org.apache.lucene.analysis.core.WhitespaceAnalyzer
           org.apache.lucene.analysis.tokenattributes.CharTermAttribute
           org.apache.lucene.util.Version
           opennlp.tools.tokenize.SimpleTokenizer
           java.io.StringReader))

(defn simple-tokenizer
  "Uses the OpenNLP SimpleTokenizer which tokenizes on character class to tokenize the given text.
Returns a vector containing the tokens."
  [text]
  (vec (.tokenize (SimpleTokenizer/INSTANCE) text)))

(defn lucene-tokenizer
  "Uses the supplied Lucene Analyzer to tokenize the given text. Returns a vector containing the tokens."
  [analyzer text]
  (let [tokenstream (.tokenStream analyzer "field" (StringReader. text))
        termatt (.addAttribute tokenstream CharTermAttribute)]
    (.reset tokenstream)
    (take-while identity (repeatedly #(when (.incrementToken tokenstream) (.toString termatt))))))

(defn standard-tokenizer  
  "Uses the Lucene StandardTokenizer to tokenize the given text. Returns a vector containing
the tokens."
  [text]
  (lucene-tokenizer (StandardAnalyzer.) text))

(defn whitespace-tokenizer
  "Uses the Lucene WhitespaceTokenizer to tokenize the given text. Returns a vector containing
the tokens."
  [text]
  (lucene-tokenizer (WhitespaceAnalyzer.) text))
