package chess;

import java.util.Collection;

public class PieceMove {

    public static void move(ChessPiece.PieceType type, ChessGame.TeamColor pieceColor, Collection<ChessMove> validMoves, ChessPosition startingPosition, ChessBoard board) {
        int[][] eightDirections = {{-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}};

        switch (type) {
            case ChessPiece.PieceType.KING:
                singularDirectionalMove(eightDirections, pieceColor, validMoves, startingPosition, board);
                break;
            case ChessPiece.PieceType.QUEEN:
                directionalMove(eightDirections, pieceColor, validMoves, startingPosition, board);
                break;
            case ChessPiece.PieceType.BISHOP:
                int[][] diagonalDirections = {{-1, -1}, {-1, 1}, {1, 1}, {1, -1}};
                directionalMove(diagonalDirections, pieceColor, validMoves, startingPosition, board);
                break;
            case ChessPiece.PieceType.KNIGHT:
                int[][] knightDirections = {{-1, -2}, {1, -2}, {2, -1}, {2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}};
                singularDirectionalMove(knightDirections, pieceColor, validMoves, startingPosition, board);
                break;
            case ChessPiece.PieceType.ROOK:
                int[][] lateralDirections = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
                directionalMove(lateralDirections, pieceColor, validMoves, startingPosition, board);
                break;
            case ChessPiece.PieceType.PAWN:
                pawnMoves(pieceColor, validMoves, startingPosition, board);
                break;
        }
    }

    public static void directionalMove(int[][] directions, ChessGame.TeamColor pieceColor, Collection<ChessMove> validMoves, ChessPosition startingPosition, ChessBoard board) {
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

    public static void singularDirectionalMove(int[][] directions, ChessGame.TeamColor pieceColor, Collection<ChessMove> validMoves, ChessPosition startingPosition, ChessBoard board) {
        for (int[] direction : directions) {
            ChessPosition nextPosition = new ChessPosition(startingPosition.getRow() + direction[0], startingPosition.getColumn() + direction[1]);
            if (nextPosition.isOnBoard()) {
                ChessPiece boardPiece = board.getPiece(nextPosition);
                if (boardPiece != null) { //nextPosition contains another piece
                    if (boardPiece.getTeamColor() != pieceColor) { //add nextPosition if the piece is opposite team
                        validMoves.add(new ChessMove(startingPosition, nextPosition, null));
                    }
                } else { //King is unblocked
                    validMoves.add(new ChessMove(startingPosition, nextPosition, null));
                }
            }
        }
    }

    public static void pawnMoves(ChessGame.TeamColor pieceColor, Collection<ChessMove> validMoves, ChessPosition startingPosition, ChessBoard board) {
        int[][] pawnDirections = {{1, 0}};
        boolean hasMoved = false;
        if (pieceColor == ChessGame.TeamColor.BLACK) {
            pawnDirections[0][0] = -1;
            if (startingPosition.getRow() != 7) {hasMoved = true;}
        } else {if (startingPosition.getRow() != 2) {hasMoved = true;}}
        singularDirectionalMove(pawnDirections, pieceColor, validMoves, startingPosition, board);
        if (!hasMoved) {int[][] enPassant = {{2*pawnDirections[0][0], 0}}; singularDirectionalMove(enPassant, pieceColor, validMoves, startingPosition, board);}
    }
}
