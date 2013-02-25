(ns nlp-tools.tokenization-tests
  (:use [nlp-tools.tokenization]
        [midje.sweet]))

(fact (simple-tokenizer "token1-token2") => ["token" "1" "-" "token" "2"])

(facts "About the simple-tokenizer"
  (simple-tokenizer "token1-token2") => ["token" "1" "-" "token" "2"])

(facts "About the standard-tokenizer"
  (standard-tokenizer "token1-token2") => ["token1" "token2"]
  (standard-tokenizer "me@here.com") => ["me" "here.com"])

(facts "About the whitespace-tokenizer"
  (whitespace-tokenizer "token1 token2") => ["token1" "token2"])
