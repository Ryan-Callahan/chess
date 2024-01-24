# Chess 240
### Ryan Callahan
<!-- TOC -->
* [Chess 240](#chess-240)
    * [Ryan Callahan](#ryan-callahan)
* [Movement Rules for Chess](#movement-rules-for-chess)
  * [Ranged Directional Moves](#ranged-directional-moves)
    * [Bishop](#bishop)
    * [Rook](#rook)
    * [Queen](#queen)
  * [Singular Directional Moves](#singular-directional-moves)
    * [Knight](#knight)
    * [King](#king)
  * [Pawns](#pawns)
* [The Chess Board](#the-chess-board)
<!-- TOC -->


# Movement Rules for Chess
Each [chess piece](shared/src/main/java/chess/ChessPiece.java) has a .pieceMoves() method that returns a HashSet of ChessMoves. <br/>
ChessPiece.pieceMoves() will call [PieceMoves](shared/src/main/java/chess/PieceMove.java).moves(), which will then figure out what moves a chess <br/>
piece can make.

## Ranged Directional Moves
This includes the Queen, Bishops, and Rooks. Directions are given in the form of x, y directional vectors.

Pseudocode:
```
for each direction a piece can move
    while the next position is on the board
        if the next position conatins other piece
            if other piece is opp. team, add next position
            else, break from while loop
        else, add the next position
```

### Bishop
Direction Vectors are all four diagonal directions
```
|-1| |-1| | 1| | 1|
|-1| | 1| |-1| | 1|
```

### Rook
Direction Vectors are all four lateral directions
```
| 0| | 0| |-1| | 1|
|-1| | 1| | 0| | 0|
```

### Queen
Direction Vectors are all eight directions
```
| 0| | 0| |-1| | 1| |-1| |-1| | 1| | 1|
|-1| | 1| | 0| | 0| |-1| | 1| |-1| | 1|
```

## Singular Directional Moves
This includes the King and the Knight. Instead of direction vectors, this algorithm <br/>
technically uses coordinates relative to the location of the piece being moved. <br/>

Pseudocode:
```
for each place a piece can move
    if that place conatins other piece
        if other piece is opp. team, add that place
    else, add that place
```

### Knight
Relative coordinates for the Knight (N) are:
```
        _______ _______ _______ _______ _______
       |_______|_______|_______|_______|_______|
       |_______|{ 2,-1}|_______|{ 2, 1}|_______|
       |{ 1, 2}|_______|_______|_______|{ 1, 2}|
       |_______|_______|___N___|_______|_______|
       |{-1,-2}|_______|_______|_______|{-1, 2}|
       |_______|{-2,-1}|_______|{-2, 1}|_______|
       |_______|_______|_______|_______|_______|
```

### King
Relative coordinates for the King (K) are:
```
        _______ _______ _______ _______ _______
       |_______|_______|_______|_______|_______|
       |_______|{ 1,-1}|{ 1, 0}|{ 1, 1}|_______|
       |_______|{ 0,-1}|___K___|{ 0, 1}|_______|
       |_______|{-1,-1}|{-1, 0}|{-1, 1}|_______|
       |_______|_______|_______|_______|_______|
```

## Pawns
Pawns are complicated in the sense that there's more functionality to a pawn than <br/>
to any other piece. Pawns use a modified version of the Singular Directional Moves <br/>
algorithm for determining the initial move (one/two spaces) and any forward move after, <br/>
since they can't capture forwards. Their diagonal capture move is almost an inverted <br/>
method from their regular moves, since they can only capture diagonally and not just move.

Pseudocode for forward moves:
```
if forwards is not blocked,
    add that place
    if that place is the edge of the board,
        promote pawn
if initial move and the next forwards place is not blocked,
    add that place as well
```

Pseudocode for diagonal captures:
```
if forwards diagonal is occupied by opp. team
    add that space
    if that space is edge of the board,
        promote pawn
```

The complication happens when checking for the correct direction (all pawns on the same team </br>
move the same direction) and when promoting a pawn for reaching the edge of the board. To avoid <br/>
rewritten code, my singularDirectionalMove function includes cases for if the piece is a pawn or not. <br/>
The diagonal capture, however, is its own function due to the contradictory nature of the pawn's <br/>
moves/captures.

# The Chess Board
The [ChessBoard](shared/src/main/java/chess/ChessBoard.java) class uses an 8x8 2-dimensional array to keep track of all the chess pieces. <br/>
Currently, the only methods ChessBoard possesses are resetBoard, addPiece, and getPiece, all of <br/>
which are pretty self-explanatory.