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
                    if (boardPiece != null) {
                        if (boardPiece.getTeamColor() != pieceColor) {
                            validMoves.add(new ChessMove(startingPosition, nextPosition, null));
                        }
                    } else {
                        validMoves.add(new ChessMove(startingPosition, nextPosition, null));
                    }
                    nextPosition = new ChessPosition(nextPosition.getRow() + direction[0], nextPosition.getColumn() + direction[1]);
                }
            }
        }
    }
}
