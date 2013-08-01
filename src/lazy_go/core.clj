(ns lazy-go.core
  (:require [clojure.core.async :refer (alts! alts!!)]))

(defn lazy-go!! [& cs]
  (let [[value channel] (alts!! cs)
        remaining (filter #(not= channel %) cs)]
    (if (empty? remaining)
      [value]
      (cons value (lazy-seq (apply lazy-go-logic remaining))))))
