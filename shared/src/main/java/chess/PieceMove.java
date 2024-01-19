package chess;

import java.util.Collection;

public class PieceMove {

    public static void move(ChessPiece.PieceType type,ChessGame.TeamColor pieceColor, Collection<ChessMove> validMoves, ChessPosition startingPosition, ChessBoard board) {
        if (type == ChessPiece.PieceType.BISHOP) {
            ChessPosition nextPosition = new ChessPosition(startingPosition.getRow(), startingPosition.getColumn());
            int row = nextPosition.getRow();
            int col = nextPosition.getColumn();

            int[] cardinality = {1, 8};

            for (int longitude : cardinality) {
                for (int latitude : cardinality) {
                    while ((row != latitude) && (col != longitude)) {
                        if (row > latitude) {
                            row = row - 1;
                        } else {
                            row = row + 1;
                        }

                        if (col > latitude) {
                            col = col - 1;
                        } else {
                            col = col + 1;
                        }

                        nextPosition = new ChessPosition(row, col);
                        ChessPiece boardPiece = board.getPiece(nextPosition);
                        if (boardPiece != null) {
                            if (boardPiece.getTeamColor() != pieceColor) {
                                validMoves.add(new ChessMove(startingPosition, nextPosition, null));
                                break;
                            }
                            break;
                        } else {
                            validMoves.add(new ChessMove(startingPosition, nextPosition, null));
                        }

                    }
                }
            }

        }
    }
}
