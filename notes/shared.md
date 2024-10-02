# Shared

## The Game of Chess

- [Chess Board](#chess-board)
- [Chess Game](#chess-game)
- [Chess Move](#chess-move)
- [Chess Piece](#chess-piece)
- [PieceMoveService](#piecemoveservice)
- [Chess Position](#chess-position)
- [Directions/Direction](#directionsdirection)

---

## Chess Board
- chessBoard 
  - represents the board using a 2-dimensional map of rows to columns to [ChessPieces](#chess-piece) 
  - chessBoard is initialized to contain null pointers and then relies on the resetBoard() method to 
  populate itself
- isPositionOnBoard() which takes a [ChessPosition](#chess-position) 
and returns true/false depending on if that position is on the board or not. 
- The resetBoard() method is also used to restart a game of chess.

---

## Chess Game

---

## Chess Move
- startPosition
- endPosition
- promotionPiece

ChessMove uses these attributes to contain information about valid moves

---

## Chess Piece
- pieceColor
- type
- pieceMoves() provides the logic for all types of pieces

### pieceMoves() 
takes a ChessBoard and a ChessPosition, determines the proper [Directions](#directionsdirection)
for each type of piece. It then passes that information to calculateMoves, or to calculatePawnMoves for pawns.

---

## PieceMoveService

### calculateMoves()
follows the pseudocode:
```
for each direction a piece can move:
    while a piece can still move in that direction:
        if the next position is on the board:
            if the next position is a piece:
                if that piece is the opposite team, add that position to the list of valid moves.
                either way, mark that direction as blocked and move on to the next direction.
            else (next position is not a piece), add that position to the list of valid moves.
        else (next position is not on the board), mark that direction as blocked and move on to the next direction.    
```

### calculatePawnMoves()
uses the methods addPawnMove() and pawnHasMoved() to help 
determine if a move is valid. The method follows the pseudocode:
```
for each direction a pawn can move:
    if that position is on the board:
        if the direction is forwards:
            check if the pawn is blocked, promotable, and/or can move two spaces, add the move appropriately.
        else (direction is diagonal):
            if there is a capturable piece diagonal to the pawn:
                add the move to the list of moves.
```
---

## Chess Position
- row
- col

---

## Directions/Direction

### Direction
- yVector
- xVector

A simple record that represents a direction in which a chess piece would move. Would've named the class
vector if that wasn't already a standard library. yVector moves along rows, xVector moves along columns.

```
"mom can we have case class?"
"we have case class at home"
case class at home:
```

### Directions
- lateralDirections
- diagonalDirections
- omniDirections
- knightDirections
- pawnDirections

Directions is a static class (companion to the Direction record) that contains arrays of Direction objects 
so there is a common location for all ChessPieces to reference when determining valid moves. Every attribute
is made static so they can be referenced without initializing the class as an object.

---

---

