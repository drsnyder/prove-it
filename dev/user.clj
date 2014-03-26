(ns user
  (:require [instaparse.core :as insta]))

(def logic-expression
  (insta/parser
    "expr = l-sub
     <l-sub> = or | and | term | not
     or = l-sub <' OR '> term
     and = l-sub <' AND '> term
     not = <'NOT '> term
     <term> = var | <'('> l-sub <')'> | l-sub
     var = #'[a-zA-Z]+'"))

(logic-expression "A OR B")
(logic-expression "A OR (B AND C)")
(logic-expression "A OR (B AND NOT C)")
(logic-expression "A OR NOT (B AND C)")
(logic-expression "NOT A OR NOT (B AND C)")

(def value-map {:var {"A" false "B" true "C" false}})

(defn lookup
  [value-map s]
  (if (sequential? s)
    (get-in value-map s)
    s))

(defn logical-or
  [value-map l r]
  (or (lookup value-map l) (lookup value-map r)))

(defn logical-and
  [value-map l r]
  (and (lookup value-map l) (lookup value-map r)))

(defn logical-not
  [value-map r]
  (not (lookup value-map r)))



(insta/transform {:or  (partial logical-or value-map) :and (partial logical-and value-map) :not (partial logical-not value-map)}
                 (logic-expression "A OR (B AND NOT C)"))
