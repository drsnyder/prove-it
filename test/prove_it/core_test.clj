(ns prove-it.core-test
  (:require [midje.sweet :refer :all]
            [instaparse.core :as insta]
            [prove-it.core :refer :as p]))

(facts :expression
  (fact
    (second (p/expression->tree "A OR B")) => truthy))
