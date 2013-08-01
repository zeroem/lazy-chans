(ns lazy-go.core
  (:require [clojure.core.async :refer (alts! alts!!)]))

(defn- lazy-go-logic [alts cs]
  (let [[value channel] (alts cs)
        remaining (filter #(not= channel %) cs)]
    (if (empty? remaining)
      [value]
      (cons value (lazy-seq (lazy-go-logic alts remaining))))))

(defn lazy-go!! [& cs]
  (lazy-go-logic alts!! cs))

(defn lazy-go! [& cs]
  (lazy-go-logic alts! cs))
