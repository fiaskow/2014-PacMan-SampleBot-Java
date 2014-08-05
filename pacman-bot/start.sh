#!/bin/bash
# Params <statefile> <playersymbol> <opponentSymbol> <printBoard> <calcmillis>
	java -ea -cp target/pacman-bot-1.0-SNAPSHOT.jar za.co.entelect.challenge.Main game.state A B false 3900
