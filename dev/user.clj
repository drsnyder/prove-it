(ns user
  (:require [instaparse.core :as insta]
            [prove-it.core :as p]
            [prove-it.expression :as e]
            [midje.repl :refer :all]))


(comment
  (p/logic-expression "A OR B")
  (p/logic-expression "A OR (B AND C)")
  (p/logic-expression "A OR (B AND NOT C)")
  (p/logic-expression "A OR NOT (B AND C)")
  (p/logic-expression "NOT A OR NOT (B AND C)")
  (p/equal? "A -> B" "(NOT A) OR B")
  )

(def value-map {"A" false "B" true "C" false})
