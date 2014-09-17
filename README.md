2014 Entelect R100K challenge
=============================

Pacman Bot Submission

This bot was my entry for the Entelect challenge. It didn't win but it was fun to implement and test against other bots.

The challenge was to submit 'n bot that can play pacman against another submitted bot and the bot that eats the most pills (on a fixed game board) wins.

The source code is not very neat as I rushed to implement all the ideas and then chopped and changed a few times as new ideas came up and some turned out not to work.

This bot uses a Minimax with Alpha-Beta pruning (Negamax implementation) algorithm. The challenge allows for a bot to take up to 4 seconds to compute and submit its next move. This means that I was able to implement Iterative Deepening to maximise the lookahead depth for each move in the allowed time frame.
