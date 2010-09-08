(ns pw.bot
  (:use [clojure.string :only (trim split-lines)]
        pw.planetwars)
  (:gen-class :main true :prefix "-"))

;; Helpers for your bot
(defn my-strongest-planets
  [pw]
  (sort-by :num-ships > (my-planets pw)))

(defn enemys-weakest-planets
  [pw]
  (sort-by :num-ships (enemy-planets pw)))

(defn my-strongest-planet-id
  [pw]
  (when-first [p (my-strongest-planets pw)]
              (p :planet-id)))

(defn weakest-enemy-planet-id
  [pw]
  (when-first [p (enemys-weakest-planets pw)]
              (p :planet-id)))

(defn ihalf [x] (int (/ x 2)))

;; Your Robot
(defn do-turn [game]
  (cond
   ;; Do nothing if a fleet is in flight
   (pos? (count (my-fleets game))) nil
   ;; Else send half your ships from your strongest planets
   ;; to your enemy's weakest planet
   :else (let [source (my-strongest-planet-id game) 
               dest (weakest-enemy-planet-id game)]
           (when-not (or (nil? source) (nil? dest))
             (issue-order source dest
                          (ihalf ((get-planet game source) :num-ships)))))))

;; Utility functions
(defn- go? [s] (= (apply str (take 2 s)) "go"))

;; Main IO loop
(defn -main [& args]
  (try
    (loop [pw ""]
      (if-let [line (read-line)]
        (cond (go? line) (if-not (empty? pw)
                           (do (do-turn (parse-game-state pw))
                               (finish-turn)
                               (recur ""))
                           (do (finish-turn)
                               (recur "")))
              :else (recur (apply str (concat pw line "\n"))))))
    (catch Exception e
      (do
        (println "There has been an error.")
        (java.lang.System/exit 1))))
  (java.lang.System/exit 0))

