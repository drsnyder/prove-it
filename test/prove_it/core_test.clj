(ns prove-it.core-test
  (:require [midje.sweet :refer :all]
            [instaparse.core :as insta]
            [prove-it.core :as p]))

(facts :expression :tree
  (first (p/expression->tree "A OR B")) => :expr
  (first (second (p/expression->tree "A OR B"))) => :or
  (second (second (p/expression->tree "A OR B"))) => [:var "A"]
  (nth (second (p/expression->tree "A OR B")) 2) => [:var "B"])

(facts :tree :tokens
  (p/tree->tokens nil) => (throws java.lang.AssertionError)
  (p/tree->tokens []) => (throws java.lang.AssertionError)
  (p/tree->tokens [:var :a]) => :a)
