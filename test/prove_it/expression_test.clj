(ns prove-it.expression-test
  (:require [midje.sweet :refer :all]
            [prove-it.expression :as e]
            [prove-it.core :as p]))

(facts :expression
  (e/expression 'a) => [:expr 'a]
  (e/expression 'a 'b) => [:expr 'a 'b]
  (e/expression 'a 'b 'c) => [:expr 'a 'b 'c])

(facts :make-node
  (e/make-node "foo") => [:var "foo"])

(facts :impl
  (e/implies 'a 'b) => [:impl (e/make-node 'a)  (e/make-node 'b)]
  (e/node->string (e/implies 'a 'b)) => "(a -> b)")

(facts :and
  (e/and* 'a 'b) => [:and (e/make-node 'a)  (e/make-node 'b)]
  (e/node->string (e/and* 'a 'b)) => "(a AND b)")

(facts :or
  (e/or* 'a 'b) => [:or (e/make-node 'a)  (e/make-node 'b)]
  (e/node->string (e/or* 'a 'b)) => "(a OR b)")

(facts :not
  (e/not* 'a) => [:not (e/make-node 'a)]
  (e/node->string (e/not* 'a)) => "(NOT a)")

(facts :biconditional
  (e/biconditional 'a 'b) => [:bicond (e/make-node 'a) (e/make-node 'b)]
  (e/node->string (e/biconditional 'a 'b)) => "(a <-> b)")

(facts :equivalence
  (e/expression (e/implies "a" "b")) => (p/logic-expression "a -> b")
  (e/expression (e/biconditional "a" "b")) => (p/logic-expression "a <-> b")
  (e/expression (e/and* "a" "b")) => (p/logic-expression "a AND b")
  (e/expression (e/or* "a" "b")) => (p/logic-expression "a OR b")
  (e/expression (e/not* "a")) => (p/logic-expression "NOT a"))
