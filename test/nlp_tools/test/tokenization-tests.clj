(ns nlp-tools.test.tokenization
  (:use [nlp-tools.tokenization]
        [midje.sweet]))

(fact (simple-tokenizer "token1-token2") => ["token" "1" "-" "token" "2"])