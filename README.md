# prove-it

A repl for logical statements.

## Usage

Are these expressions equal?

  (:require [prove-it.core :as p])

  (p/equal? "A -> B" "NOT A OR B")
  => true

## License

Copyright © 2014 Damon Snyder

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
