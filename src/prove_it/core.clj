(ns prove-it.core
  (:require [instaparse.core :as insta]
            [clojure.math.combinatorics :as combo]))

(def logic-expression
  (insta/parser
    "expr = l-sub
     <l-sub> = or | and | term | not
     or = l-sub <' OR '> term
     and = l-sub <' AND '> term
     not = <'NOT '> term
     <term> = var | <'('> l-sub <')'> | l-sub
     var = #'[a-zA-Z]+'"))

(def operators #{:or :and :not})

(defn tree->tokens
  ([tree acc]
  (let [expr (first tree)]
    (cond
      (= expr :expr) (tree->tokens (second tree))
      (operators expr) (concat acc (flatten (map tree->tokens (subvec tree 1))))
      :else (first (subvec tree 1)))))
  ([tree]
   (tree->tokens tree (list))))

(defn table-inputs
  [vars]
  (let [nvars (count vars)]
    (map #(zipmap vars %) (apply combo/cartesian-product (take nvars (cycle [[true false]]))))))

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

(defn expression->tree
  [expression]
  (insta/transform {} (logic-expression expression)))

(defn evaluate
  [expression value-map]
  (second (insta/transform {:or  (partial logical-or value-map)
                    :and (partial logical-and value-map)
                    :not (partial logical-not value-map)}
                   (logic-expression expression))))



