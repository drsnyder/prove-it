(ns user
  (:require [instaparse.core :as insta]
            [prove-it.core :as p]
            [midje.repl :refer :all]))


(p/logic-expression "A OR B")
(p/logic-expression "A OR (B AND C)")
(p/logic-expression "A OR (B AND NOT C)")
(p/logic-expression "A OR NOT (B AND C)")
(p/logic-expression "NOT A OR NOT (B AND C)")

(def value-map {"A" false "B" true "C" false})
