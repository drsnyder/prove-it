# prove-it

A repl for logical statements.

## Usage

To load into your namespace:

```clojure
(:require (prove-it [core :as p]
                    [expression :as e]))
```

Some examples. Are these expressions equal?
    
```clojure
(p/equal? "A -> B" "(NOT A) OR B") => true
```

Is the following argument valid?

```clojure
; (p/valid? conclusion & propositions)
(p/valid? "NOT (S AND E)" "S OR E" "S -> H" "E -> NOT H") => true
(p/valid? "W <-> P" "W <-> (P AND C)" "NOT C") => false
```

Generate an expression tree using s-expressions:

```clojure
(e/expression (e/implies “A” “B”)) => [:expr [:impl [:var “A”] [:var “B”]]]
```

Construct an expression using s-expressions and compare that with a string.

```clojure
(p/equal? (p/logic-expression “A -> B”) (e/expression (e/implies “A” “B”))) => true
```

## License

Copyright © 2014 Damon Snyder

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
