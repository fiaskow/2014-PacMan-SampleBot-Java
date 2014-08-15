#!/bin/bash
START="true"
COUNTER=0
cp initial.state game.state
rm *internal.state
while [ $COUNTER -eq 0 ]; do
# Params <statefile> <playersymbol> <opponentSymbol> <printBoard> <calcmillis>
	java -ea -cp target/pacman-bot-1.0-SNAPSHOT.jar za.co.entelect.challenge.Main game.state A B true 3850 true 2> analyse.txt
#sed -i '' 's/!/./g' game.state
sleep 1
	java -ea -cp target/pacman-bot-1.0-SNAPSHOT.jar za.co.entelect.challenge.Main game.state B A true 3850 true 2> analyse.txt
#sed -i '' 's/!/./g' game.state
#	sleep 1
	START="false"
done
