(ns lazy-chans.core
  (:require [clojure.core.async :refer (go <! alts! alts!!)]))

(defn lazy-chans!! [cs]
  (lazy-seq
    (let [[value channel] (alts!! cs)
          remaining (filter #(not= channel %) cs)]
      (if (empty? remaining)
        [value]
        (cons value (lazy-chans!! remaining))))))

(defn- reduce-additional-messages [a f ch]
  (go
    (loop []
      (if-let [msg (<! ch)]
        (do
          (swap! a f msg)
          (recur))))))

(defn reduce-chan
  ([f v ch] (let [result (atom v)]
    (go
      (if-let [initial-message (<! ch)]
        (do
          (swap! result f initial-message)
          (reduce-additional-messages result f ch))
        (reset! result (f v))))
    result))

  ([f ch] (let [result (atom nil)]
    (go
      (if-let [initial-message (<! ch)]
        (if-let [second-message (<! ch)]
          (do
            (reset! result (f initial-message second-message))
            (reduce-additional-messages result f ch))
          (reset! result initial-message))
        (reset! result (f))))
    result)))
