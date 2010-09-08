[clojure](http://clojure.org) language pack for the latest contest hosted at [ai-content.com](http://ai-contest.com)

## How to use this bot:

All build functionality achieved through [leiningen](http://github.com/technomancy/leiningen). A copy of the current release (1.3.1) is included in the bin directory, both for Unix and Windows machines.

All you need to build the clojure bot is a simple "lein uberjar" command, the rest belongs to leiningen's magic. You don't even need to add the script to your path just call it with a relative path.
Unix:
    $ ./bin/lein uberjar
Windows:
    $ .\bin\lein.bat uberjar

The output is a single file called cljbot.jar which should run with "java -jar cljbot.jar". As clojure code starts up slower then needed you must adjust max_turn_time through the command line as a workaround. Please note, this is a benifit for this setup as max_turn_time is valid for each turn, this must be fixed by the judges somehow.
    $ java -jar tools/PlayGame.jar maps/map7.txt 3000 1000 log.txt "java -jar example_bots/RandomBot.jar" "java -jar cljbot.jar" | java -jar tools/ShowGame.jar

## Notes

Note that the start-up time is 3000ms instead of 1000; this is because
it takes about that long for the JVM to get going with Clojure. If the
bot times out, there's a chance you'll need to increase the time. 

Hopefully there will be an update to the game engine that doesn't penalize slow start-up times (e.g. both bots send "OKAY" when they're all ready, *then* recieve the map/game-state from the game engine.)

The contest is hosted at [ai-content.com](http://ai-contest.com)

* * *

Please report any errors or bugs at http://github.com/ihodes/ai-contest-planet-wars-clj
