(ns nlp-tools.tokenization
  (:import org.apache.lucene.analysis.standard.StandardAnalyzer
           org.apache.lucene.analysis.WhitespaceAnalyzer
           org.apache.lucene.analysis.tokenattributes.TermAttribute
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
  (let [ tokenstream (.tokenStream analyzer "field" (StringReader. text))
        termatt (.addAttribute tokenstream TermAttribute)]
    (take-while identity (repeatedly #(when (.incrementToken tokenstream) (.term termatt))))))

(defn standard-tokenizer  
  "Uses the Lucene StandardTokenizer to tokenize the given text. Returns a vector containing
the tokens."
  [text]
  (lucene-tokenizer (StandardAnalyzer. Version/LUCENE_31) text))

(defn whitespace-tokenizer
  "Uses the Lucene WhitespaceTokenizer to tokenize the given text. Returns a vector containing
the tokens."
  [text]
  (lucene-tokenizer (WhitespaceAnalyzer. Version/LUCENE_31) text))
