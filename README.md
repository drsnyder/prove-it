# prove-it

A repl for logical statements.

## Usage

Are these expressions equal?

   (:require [prove-it.core :as p])

   (p/equal? "A -> B" "NOT A OR B")
   => true

Is the following argument valid?

   ; (p/valid? conclusion & propositions)
   (p/valid? "NOT (S AND E)" "S OR E" "S -> H" "E -> NOT H")
   => true
   (p/valid? "W <-> P" "W <-> (P AND C)" "NOT C")
   => false

## License

Copyright © 2014 Damon Snyder

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
