(defproject clj-pwars "1.0.0-SNAPSHOT"
  :description "Clojure starter package for planet-wars Google ai contest."
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [log4j "1.2.15" :exclusions  [ javax.mail/mail
                              javax.jms/jms
                              com.sun.jdmk/jmxtools
                              com.sun.jmx/jmxri]]
                 [org.slf4j/slf4j-api "1.6.1"]
                 [org.slf4j/slf4j-log4j12 "1.6.1"]
  ]
  :resources-path "conf"
  :uberjar-name "cljbot.jar"
  :aot [pw.my_bot pw.planetwars]
  :main pw.bot
  :disable-implicit-clean true
)
