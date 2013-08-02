(ns lazy-chans.core-test
  (:require [clojure.test :refer :all]
            [lazy-chans.core :refer :all]
            [clojure.core.async :refer (close! go chan >!!)]))

(deftest lazy-seq-of-channel-messages
  (testing "lazy-chans!! returns a lazy-seq of channel results"
           (let [s (lazy-chans!! [(go 1) (go 2) (go 3)])]
             (is (= '(1 2 3) (sort s)))
             (is (= 3 (count s))))))

(deftest incremental-reudce-chan
  (testing "can reduce with no messages"
           (let [f (fn [] true)
                 ch (chan)
                 result (reduce-chan f ch)]
             (close! ch)
             (Thread/sleep 100)
             (is (= true @result))))

  (testing "can reduce with one messages"
           (let [f (fn [a] a)
                 ch (chan)
                 result (reduce-chan f ch)]
             (>!! ch true)
             (close! ch)
             (Thread/sleep 100)
             (is (= true @result))))

  (testing "can reduce two messages"
           (let [f +
                 ch (chan)
                 result (reduce-chan f ch)]
             (>!! ch 1)
             (>!! ch 2)
             (Thread/sleep 100)
             (is (= 3 @result))))

   (testing "can reduce more messages"
           (let [f +
                 ch (chan)
                 result (reduce-chan f ch)]
             (>!! ch 1)
             (>!! ch 2)
             (>!! ch 3)
             (close! ch)
             (Thread/sleep 100)
             (is (= 6 @result))))

   (testing "can reduce with initial value and no messages"
           (let [f (fn [a] a)
                 ch (chan)
                 result (reduce-chan f 1 ch)]
             (close! ch)
             (Thread/sleep 100)
             (is (= 1 @result))))

   (testing "can reduce with initial value and one message"
           (let [f +
                 ch (chan)
                 result (reduce-chan f 1 ch)]
             (>!! ch 1)
             (close! ch)
             (Thread/sleep 100)
             (is (= 2 @result))))

   (testing "can reduce with initial value and more message"
           (let [f +
                 ch (chan)
                 result (reduce-chan f 1 ch)]
             (>!! ch 1)
             (>!! ch 1)
             (close! ch)
             (Thread/sleep 100)
             (is (= 3 @result)))))
