(ns pw.bot
  (:use [clojure.string :only (trim split-lines)]
        pw.planetwars))

;; Helpers for your bot
(defn my-strongest-planet
  [pw]
  (let [p (first (sort-by :num-ships > (my-planets pw)))]
    (if-not (nil? p)
      (p :planet-id))))

(defn weakest-enemy-planet
  [pw]
  (let [p (first (sort-by :num-ships (enemy-planets pw)))]
    (if-not (nil? p)
      (p :planet-id))))

(defn ihalf [x] (int (/ x 2)))

;; Your Robot
(defn do-turn [pw]
  (cond
   ;; Do nothing if a fleet is in flight
   (pos? (count (my-fleets pw))) nil
   ;; Else send half your ships from your strongest planets
   ;; to your enemy's weakest planet
   :else (let [source (my-strongest-planet pw) 
               dest (weakest-enemy-planet pw)]
           (if (or (nil? source) (nil? dest))
             nil
             (issue-order source dest
                          (ihalf ((get-planet pw source) :num-ships)))))))


;; Utility functions
(defn- go? [s] (= (apply str (take 2 s)) "go"))
(defn- take-turn
  [f pw]
  (f (parse-game-state pw)) ;; run your bot (f) on parsed pw
  (finish-turn)) ;; say go

;; Main IO loop
(defn -main [& args]
  (try (loop [line (read-line) pw ""]
         (cond (go? line) (if-not (empty? pw)
                            (do
                              (take-turn do-turn pw)
                              (recur (read-line) ""))
                            (do
                              (finish-turn)
                              (recur (read-line) "")))
               :else (recur (read-line)
                            (apply str (concat pw line "\n")))))
       (catch Exception e
         (println "Egregious error, man. Egreege."))))

;; Run the program
;; (try (-main)
;;      (catch Exception e
;;        (println "And we're done.")))

