(ns prove-it.core
  (:require [instaparse.core :as insta]
            [clojure.math.combinatorics :as combo]))

; FIXME move to a resource as logic-expression.bnf
(def logic-expression
  (insta/parser
    "expr = l-sub
     <l-sub> = or | and | term | not
     or = l-sub <' OR '>  term | l-sub <' or '> term
     and = l-sub <' AND '> term | l-sub <' and '> term
     not = <'NOT '> term | <'not '> term
     <term> = var | <'('> l-sub <')'> | l-sub
     var = #'[a-zA-Z]+'"))

(def operators #{:or :and :not})

(defn logical-or
  [l r]
  (or l r))

(defn logical-and
  [l r]
  (and l r))

(defn logical-not
  [r]
  (not r))

(defn expression->tree
  [expression]
  (insta/transform {} (logic-expression expression)))

(defn evaluate
  [expression value-map]
  (second (insta/transform {:var value-map
                            :or  logical-or
                            :and logical-and
                            :not logical-not}
                           (logic-expression expression))))

(defn tree->tokens-list
  ([tree acc]
   {:pre  [(sequential? tree) (not-empty tree)]}
   (let [expr (first tree)]
     (cond
       (= expr :expr) (tree->tokens-list (second tree))
       (operators expr) (concat acc (flatten (map tree->tokens-list (subvec tree 1))))
       :else (first (subvec tree 1)))))
  ([tree]
   (tree->tokens-list tree (list))))

(defn tree->tokens
  [tree]
  (set (tree->tokens-list tree (list))))

(defn table-inputs
  [vars]
  (let [nvars (count vars)]
    (map #(zipmap vars %) (apply combo/cartesian-product (take nvars (cycle [[true false]]))))))

