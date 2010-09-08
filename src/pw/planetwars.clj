(ns pw.planetwars
  (:use [clojure.contrib.math :only (sqrt ceil)]
        [clojure.string :only (join split-lines split trim)]))

;; Fleet
(defstruct fleet
  :owner :num-ships :source-planet :destination-planet
  :total-trip-length :turns-remaining)

;; Planet
(defstruct planet
  :planet-id :x :y :owner :num-ships :growth-rate)

;; Game
(defstruct #^{:doc "This is the overall state of the game."}
  planet-wars-game :planets :fleets)

(defn add-ships
  "Returns 'planet with 'num-ships added to it."
  [planet num-ships]
  (assoc planet
    :num-ships (+ (planet :num-ships) num-ships)))

(defn remove-ships
  "Returns 'planet with 'num-ships removed from it."
  [planet num-ships]
  (assoc planet
    :num-ships (- (planet :num-ships) num-ships)))

(defn- sq [x] (* x x))

(defn distance
  "Returns the (int) Euclidean distance between 'source-planet-id and
   'dest-planet-id."
  [source-planet-id dest-planet-id]
  (let [dx (- (source-planet-id :x) (dest-planet-id :x))
        dy (- (source-planet-id :y) (dest-planet-id :y))]
    (int (ceil (sqrt (+ (sq dx)
                        (sq dy)))))))

(defn fleets
  "Returns a seq of all the fleet in 'game'"
  [game]
  (game :fleets))

(defn planets
  "Returns a seq of planets in 'game."
  [game]
  (game :planets))

(defn num-planets
  "Returns the number of planets in the game."
  [game]
  (count (planets game)))

(defn num-fleets
  "Returns the number of fleets en route."
  [game]
  (count (fleets game)))

(defn get-planet
  "Returns the planet with given id."
  [game planet-id]
  (first (filter #(= (:planet-id %) planet-id)
                 (planets game))))

(defn get-fleet ;; fleets don't have ids...
  "Returns the fleet with given id."
  [game fleet-id]
  (get (game :fleets) fleet-id))

(defn- my? [x] (== 1 (x :owner)))
(defn- neut? [x] (== 0 (x :owner)))
(defn- enemy? [x] (< 1 (x :owner)))

(defn my-planets
  "Returns a seq of all the planets owned by you."
  [game]
  (filter my? (planets game)))

(defn neutral-planets
  "Returns a seq of all the neutral planets in game."
  [game]
  (filter neut? (planets game)))

(defn enemy-planets
  "Returns a seq of all the enemy planets in the game."
  [game]
  (filter enemy? (planets game)))

(defn not-my-planets
  "Returns a seq of all the planets in game not owned by you."
  [game]
  (remove my? (planets game)))

(defn my-fleets
  "Returns a seq of all of your fleets."
  [game]
  (filter my? (fleets game)))

(defn enemy-fleets
  "Returns a seq of all of the enemy's fleets."
  [game]
  (filter enemy? (fleets game)))

(defn is-alive?
  "Returns true if player-id is alive."
  [game player-id]
  (some (partial = player-id)
        (map :owner (concat (planets game)
                            (fleets game)))))

;;;;; IO functions:
(defn to-string
  "Prints the state of the game as a string."
  [game]
  (let [pstr (for [p (planets game)]
               (join \space (list "P" (p :planet-id) (p :x) (p :y) (p :owner)
                    (p :num-ships) (p :growth-rate))))
        fstr (for [f (fleets game)]
               (join \space (list "F" (f :owner) (f :num-ships) (f :source-planet)
                    (f :destination-planet) (f :total-trip-length)
                    (f :turns-remaining))))]
        (str (join \newline (concat pstr fstr)) \newline)))

(defn issue-order
  "Issues an order, sending 'num-ships from source-planet to
   'dest-planet."
  [source-planet dest-planet num-ships]
  (prn source-planet dest-planet num-ships))

(defn finish-turn
  "Finishes turn."
  []
  (prn 'go))

(defn- to_i [x] (Integer. x))
(defn- to_d [x] (Double. x))

(defn s-to-planet
  "Returns a planet struct with planet-id 'id from 's."
  [s id]
  (let [st (vec (drop 1 (split s #" ")))]
    (struct planet id (to_d (st 0)) (to_d (st 1)) (to_i (st 2)) (to_i (st 3)) (to_i (st 4)))))

(defn s-to-fleet
  "Returns a fleet struct from 's."
  [s]
  (apply struct fleet
         (map to_i (drop 1 (split s #" ")))))

(defn- clean [s] (trim (get (split s #"#") 0)))
(defn- p-line? [s] (= \P (first s)))
(defn- f-line? [s] (= \F (first s)))

(defn parse-game-state
  "Returns a new planet-wars-game struct from 's"
  [s]
  (loop [planets [] fleets [] pid 0 lines (map clean (split-lines s))]
    (cond (empty? lines) (struct planet-wars-game planets fleets)
          (p-line? (first lines)) (recur
                                   (cons (s-to-planet (first lines) pid) planets)
                                   fleets
                                   (inc pid)
                                   (rest lines))
          (f-line? (first lines)) (recur
                                   planets
                                   (cons (s-to-fleet (first lines)) fleets)
                                   pid
                                   (rest lines)))))
