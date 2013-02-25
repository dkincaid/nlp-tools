(ns nlp-tools.tokenization
  (:import org.apache.lucene.analysis.standard.StandardAnalyzer
           org.apache.lucene.analysis.WhitespaceAnalyzer
           org.apache.lucene.analysis.tokenattributes.TermAttribute
           org.apache.lucene.util.Version
           opennlp.tools.tokenize.SimpleTokenizer
           java.io.StringReader))

(defn simple-tokenizer [text]
  "Uses the OpenNLP SimpleTokenizer which tokenizes on character class to tokenize the given text.
Returns a vector containing the tokens."
  (vec (.tokenize (SimpleTokenizer/INSTANCE) text)))

(defn lucene-tokenizer [analyzer text]
  "Uses the supplied Lucene Analyzer to tokenize the given text. Returns a vector containing the tokens."
    (let [ tokenstream (.tokenStream analyzer "field" (StringReader. text))
        termatt (.addAttribute tokenstream TermAttribute)]
    (take-while identity (repeatedly #(when (.incrementToken tokenstream) (.term termatt))))))

(defn standard-tokenizer [text]
  "Uses the Lucene StandardTokenizer to tokenize the given text. Returns a vector containing
the tokens."
  (lucene-tokenizer (StandardAnalyzer. Version/LUCENE_31) text))

(defn whitespace-tokenizer [text]
  "Uses the Lucene WhitespaceTokenizer to tokenize the given text. Returns a vector containing
the tokens."
  (lucene-tokenizer (WhitespaceAnalyzer. Version/LUCENE_31) text))
