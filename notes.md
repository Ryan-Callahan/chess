# Table of Contents
I wish everything was written in Scala :(
1. [Shared](#shared)
2. [Server](#server)
3. [Client](#client)
4. [README](README.md)

---

---

# Shared

## src.main.java.chess

- [Chess Game](#chess-game)
- [Chess Board](#chess-board)
- [Chess Move](#chess-move)
- [Chess Piece](#chess-piece)
- [PieceMoveService](#piecemoveservice)
- [Chess Position](#chess-position)
- [Directions/Direction](#directionsdirection)

---

## Chess Game
- board
- [getTeamTurn()](#getteamturn)
- [setTeamTurn()](#setteamturn)
- [validMoves()](#validmoves)
- [makeMove()](#makemove)
- [isInCheck()](#isincheck)
- [isInCheckmate()](#isincheckmate)
- [isInStalemate()](#isinstalemate)
- [setBoard()](#setboard)
- [getBoard()](#getboard)

### getTeamTurn()

### setTeamTurn()

### validMoves()


### makeMove()
takes a ChessMove, ensures that the move can be legally made, and then calls movePiece to actually move the piece.

### isInCheck()
calculates if the given team is in check by checking if any of the other team's pieces are threatening the king.

### isInCheckmate()
calculates if a given team is in checkmate by checking if any possible move would remove the king from check. \
Relies on isInCheckAfterMove to determine if the team would still be in check after any specific move.

### isInStalemate()

### setBoard()

### getBoard()

---

## Chess Board
- chessBoard 
  - represents the board using a 2-dimensional map of rows to columns to [ChessPieces](#chess-piece) 
  - chessBoard is initialized to contain null pointers and then relies on the resetBoard() method to \
  populate itself
- whitePieces
  - a hashset of all positions the white team holds
- blackPieces
  - a hashset of all positions the black team holds
- addPiece()
- removePiece()
- getPiece()
  getTeamSet()
- resetBoard()

### addPiece()
adds the given piece to the given position. If the position is not empty beforehand, it calls removePiece \
with the given position and then adds the new piece. Calls addPositionToTeam if the piece being added is not null.

### removePiece()
calls removePositionFromTeam with the given position, and then uses addPiece to add null to the position.

### getPiece()
returns the piece (or null) that is in the given position.

### getTeamSet()
returns a copy of the set of positions for the given team (whitePieces or blackPieces)

### resetBoard()
resets chessBoard, whitePieces, and blackPieces to be the default for the beginning of a game. Calls \
resetTeams to reset the team position sets.

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
takes a ChessBoard and a ChessPosition, determines the proper [Directions](#directionsdirection) for each type \
of piece. It then passes that information to calculateMoves, or to calculatePawnMoves for pawns.

---

## PieceMoveService
- calculateMoves()
- calculatePawnMoves()

### calculateMoves()
This method also has an overloaded signature that defaults the continuous parameter to false. \
Follows the pseudocode:
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
uses the methods addPawnMove() and pawnHasMoved() to help determine if a move is valid. \
The method follows the pseudocode:
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

A simple record that represents a direction in which a chess piece would move. Would've named the class \
vector if that wasn't already a standard library. yVector moves along rows, xVector moves along columns.

### Directions
- lateralDirections
- diagonalDirections
- omniDirections
- knightDirections
- pawnDirections

Directions is a static class (companion to the Direction record) that contains arrays of Direction objects \
so there is a common location for all ChessPieces to reference when determining valid moves. Every attribute \
is made static so they can be referenced without initializing the class as an object.

---

---

# Server

[Chess Server Sequence Diagram](Ryan%20Callahan%20-%20Chess%20Server%20Sequence%20Diagram.png)

## package name here


