(ns lazy-chans.core-test
  (:require [clojure.test :refer :all]
            [lazy-chans.core :refer :all]
            [clojure.core.async :refer (go)]))

(deftest lazy-seq-of-channel-messages
  (testing "lazy-chans!! returns a lazy-seq of channel results"
           (let [s (lazy-chans!! (go 1) (go 2) (go 3))]
             (is (= '(1 2 3) (sort s)))
             (is (= 3 (count s))))))
