#!/bin/sh
# Based on http://ai-contest.com/forum/viewtopic.php?p=2395#p2395

RUN_JAR='java -jar'
MY_BOT=${MY_BOT:-"${RUN_JAR} ./cljbot.jar"}

for file in example_bots/*.jar
do
    player_1_counter=0
    player_2_counter=0
    for i in {1..100}
    do
        RES=`${RUN_JAR} tools/PlayGame.jar maps/map${i}.txt 3000 1000 log.txt "${RUN_JAR} $file" "${MY_BOT}" 2>&1 | grep ^Player`
        if [ "${RES}" = "Player 1 Wins!" ] ; then
            player_1_counter=`expr ${player_1_counter} + 1`
        else
            player_2_counter=`expr ${player_2_counter} + 1`
        fi
    done
    echo "${file} : ${player_2_counter}/100"
done
