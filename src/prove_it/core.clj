(ns prove-it.core
  (:require [instaparse.core :as insta]
            [clojure.math.combinatorics :as combo]))

; FIXME move to a resource as logic-expression.bnf
(def logic-expression
  (insta/parser
    "expr = l-sub
     <l-sub> = or | and | impl | term | not
     or = l-sub <' OR '>  term | l-sub <' or '> term
     and = l-sub <' AND '> term | l-sub <' and '> term
     impl = l-sub <' -> '> term
     not = <'NOT '> term | <'not '> term
     <term> = var | <'('> l-sub <')'> | l-sub
     var = #'[a-zA-Z]+'"))

(def operators #{:or :and :not :impl})

(defn logical-or
  [l r]
  (or l r))

(defn logical-and
  [l r]
  (and l r))

(defn logical-not
  [r]
  (not r))

(defn logical-implication
  [a b]
  (or (not a) b))

(defn expression->tree
  [expression]
  (logic-expression expression))

(defmulti evaluate (fn [i value-map] (class i)))

(defmethod evaluate clojure.lang.PersistentVector [tree value-map]
  (second (insta/transform {:var value-map
                            :or  logical-or
                            :and logical-and
                            :impl logical-implication
                            :not logical-not}
                           tree)))

(defmethod evaluate java.lang.String [expression value-map]
  (second (insta/transform {:var value-map
                             :or  logical-or
                             :and logical-and
                             :not logical-not}
                            (logic-expression expression))))

(defmethod evaluate :default [expression value-map]
  (throw (IllegalArgumentException. (str "Error, don't know how to evaluate " (class expression)))))


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
    (map #(zipmap vars %)
         (apply combo/cartesian-product (take nvars (cycle [[true false]]))))))

(defn equal?
  [a b]
  (let [tree-a (logic-expression a)
        a-tokens (tree->tokens tree-a)
        tree-b (logic-expression b)
        b-tokens (tree->tokens tree-b)
        _ (assert (= a-tokens b-tokens) (str "Error, expressions cannot be compared: " a-tokens " " b-tokens))
        inputs (table-inputs a-tokens)]
     (not (some false? (map #(= (evaluate tree-a %) (evaluate tree-b %)) inputs)))))
