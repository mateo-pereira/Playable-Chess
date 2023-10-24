# Playable-Chess
Fully playable chess application using the terminal

Input for making a move example would be e2 e4. If the move is illegal, the terminal will prompt INVALID MOVE and return the same board with the same player able to move.

Types of moves:

Draw: The player must type in a valid move, followed with "draw?". Example: "e2 e4 draw?".
Currently, if a player prompts a draw, it is automatically accepted.

Resign: A player simply types "resign", and the terminal prompts "RESIGN_BLACK_WINS" or "RESIGN_WHITE_WINS" depending on which player resigns.

Castling: Both players can castle on either side (King or Queen side)

Check and Checkmate: If a player has a piece attacking the opponent's king, the resulting prompt wil be "Check". The opponent player must make a move that results in their king not being put in check, or else the terminal will prompt "INVALID MOVE".

Enpassant

Pawn Promotion

![image](https://github.com/mateo-pereira/Playable-Chess/assets/100048059/d90861eb-099a-4336-a55e-5f83a66f692e)
