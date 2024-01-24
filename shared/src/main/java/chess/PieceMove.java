package chess;

import java.util.Collection;

public class PieceMove {

    public static void move(ChessPiece.PieceType type,ChessGame.TeamColor pieceColor, Collection<ChessMove> validMoves, ChessPosition startingPosition, ChessBoard board) {
        if (type == ChessPiece.PieceType.BISHOP) {

            int[][] directions = {{-1, -1}, {-1, 1}, {1, 1}, {1, -1}};

            for (int[] direction : directions) {
                ChessPosition nextPosition = new ChessPosition(startingPosition.getRow() + direction[0], startingPosition.getColumn() + direction[1]);
                int row = nextPosition.getRow();
                int col = nextPosition.getColumn();

                while (nextPosition.isOnBoard()) {
                    ChessPiece boardPiece = board.getPiece(nextPosition);
                    if (boardPiece != null) { //nextPosition contains another piece
                        if (boardPiece.getTeamColor() != pieceColor) { //add nextPosition if the piece is opposite team
                            validMoves.add(new ChessMove(startingPosition, nextPosition, null));
                        }
                        break; //don't continue if the bishop is blocked
                    } else { //Bishop is unblocked
                        validMoves.add(new ChessMove(startingPosition, nextPosition, null));
                    }
                    nextPosition = new ChessPosition(nextPosition.getRow() + direction[0], nextPosition.getColumn() + direction[1]); //continues the iteration
                }
            }
        }
    }
}
