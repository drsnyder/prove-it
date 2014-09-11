(ns prove-it.expression)

(defn expression
  [&[syms]]
  [:expr syms])

(declare make-var)

(defn implies
  [a b]
  [:implies (make-var a) (make-var b)])

(defn and*
  [a b]
  [:and (make-var a) (make-var b)])

(declare lhs)
(declare rhs)
(declare paren-wrap)

(defmulti node->string first)

(defmethod node->string :implies
  [e]
  (paren-wrap (str (lhs e) " -> " (rhs e))))

(defmethod node->string :and
  [e]
  (paren-wrap (str (lhs e) " AND " (rhs e))))

(defmethod node->string :var
  [e]
  (str (second e)))

(defn lhs
  [e]
  (node->string (first (rest e))))

(defn rhs
  [e]
  (node->string (second (rest e))))

(defn var-node
  [v]
  [:var v])

(defn var-node?
  [n]
  (and (sequential? n) (= (first n) :var)))

(defn expr-node
  [e &[syms]]
  (apply vector e syms))

(defn expr-node?
  [n]
  (and (sequential? n) (not (var-node? n))))

(defmulti make-var class)

(defmethod make-var String
  [v]
  (var-node v))

(defmethod make-var clojure.lang.Symbol
  [v]
  (var-node v))

(defmethod make-var :default
  [v]
  v)

(defn paren-wrap
  [e]
  (str "(" e ")"))
