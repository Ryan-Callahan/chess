# My Notes
by Ryan Callahan
- some stuff
- I liek programing

# Movement Rules
Each chess piece has a .pieceMoves() method that returns a HashSet of ChessMoves.

## Bishop
Pseudocode
```
for each direction a bishop can move
    while the next position is on the board
        if the next position conatins a piece
            if piece is opp. team, add next position
            else, break from while loop
        else, add the next position
```
