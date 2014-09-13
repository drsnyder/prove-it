(ns prove-it.expression)

(defn expression
  [& syms]
  (vec (concat [:expr] syms)))

(declare make-node)

(defn implies
  [a b]
  [:impl (make-node a) (make-node b)])

(defn or*
  [a b]
  [:or (make-node a) (make-node b)])

(defn and*
  [a b]
  [:and (make-node a) (make-node b)])

(defn not*
  [a]
  [:not (make-node a)])

(defn biconditional
  [a b]
  [:bicond (make-node a) (make-node b)])

(declare lhs)
(declare rhs)
(declare single)
(declare wrap)

(defmulti node->string first)

(defmethod node->string :impl
  [e]
  (wrap (str (lhs e) " -> " (rhs e))))

(defmethod node->string :bicond
  [e]
  (wrap (str (lhs e) " <-> " (rhs e))))

(defmethod node->string :and
  [e]
  (wrap (str (lhs e) " AND " (rhs e))))

(defmethod node->string :or
  [e]
  (wrap (str (lhs e) " OR " (rhs e))))

(defmethod node->string :not
  [e]
  (wrap (str "NOT " (single e))))

(defmethod node->string :var
  [e]
  (str (second e)))

(defn single
  [e]
  (node->string (first (rest e))))

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

(defmulti make-node class)

(defmethod make-node String
  [v]
  (var-node v))

(defmethod make-node clojure.lang.Symbol
  [v]
  (var-node v))

(defmethod make-node :default
  [v]
  v)

(defn wrap
  [e]
  (str "(" e ")"))
