# lazy-chans

A Clojure library designed to turn a list of go blocks into a lazy-seq

## Usage

(require '[lazy-chans/core :refer (layz-go!!)])

(lazy-chans!! (go 1) (go 2) (go 3))

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
