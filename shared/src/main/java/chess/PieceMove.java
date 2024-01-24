package chess;

import java.util.Collection;

public class PieceMove {

    public static void move(ChessPiece.PieceType type, ChessGame.TeamColor pieceColor, Collection<ChessMove> validMoves, ChessPosition startingPosition, ChessBoard board) {
        int[][] eightDirections = {{-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}};

        switch (type) {
            case ChessPiece.PieceType.KING:
                singularDirectionalMove(eightDirections, pieceColor, validMoves, startingPosition, board, false);
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
                singularDirectionalMove(knightDirections, pieceColor, validMoves, startingPosition, board, false);
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

            while (nextPosition.isOnBoard()) {
                if (isOccupied(nextPosition, pieceColor, validMoves, startingPosition, board)) {
                    capture(nextPosition, pieceColor, validMoves, startingPosition, board, null);
                    break;
                } else {
                    validMoves.add(new ChessMove(startingPosition, nextPosition, null));
                }
                nextPosition = new ChessPosition(nextPosition.getRow() + direction[0], nextPosition.getColumn() + direction[1]); //continues the iteration
            }
        }
    }

    public static void singularDirectionalMove(int[][] directions, ChessGame.TeamColor pieceColor, Collection<ChessMove> validMoves, ChessPosition startingPosition, ChessBoard board, boolean isPawn) {
        for (int[] direction : directions) {
            ChessPosition nextPosition = new ChessPosition(startingPosition.getRow() + direction[0], startingPosition.getColumn() + direction[1]);
            if (nextPosition.isOnBoard()) {
                if (isOccupied(nextPosition, pieceColor, validMoves, startingPosition, board)) {
                    if (!isPawn) {
                        capture(nextPosition, pieceColor, validMoves, startingPosition, board, null);
                    } else {break;}
                } else {
                    if (isPawn && (nextPosition.getRow() == 8 || nextPosition.getRow() == 1)) {
                        ChessPiece.PieceType[] possiblePromotions = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN};
                        for (var pieceType : possiblePromotions) {
                            validMoves.add(new ChessMove(startingPosition, nextPosition, pieceType));
                        }
                    } else { validMoves.add(new ChessMove(startingPosition, nextPosition, null)); }
                }
            }
        }
    }


    public static void pawnMoves(ChessGame.TeamColor pieceColor, Collection<ChessMove> validMoves, ChessPosition startingPosition, ChessBoard board) {
        int vector = 1;
        boolean hasMoved = false;
        ChessPiece.PieceType[] possiblePromotions = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN};

        if (pieceColor == ChessGame.TeamColor.BLACK) {
            vector = -1;
            if (startingPosition.getRow() != 7) {hasMoved = true;}
        } else {
            if (startingPosition.getRow() != 2) {hasMoved = true;}
        }

        int[][] pawnForwards = {{vector, 0}};
        int[][] pawnDiagonal = {{vector, -1}, {vector, 1}};
        if (!hasMoved) {
            int[][] enPassant = {{vector, 0}, {2*vector, 0}};
            singularDirectionalMove(enPassant, pieceColor, validMoves, startingPosition, board, true);
        } else {singularDirectionalMove(pawnForwards, pieceColor, validMoves, startingPosition, board, true);}

        for (int[] place : pawnDiagonal) {
            ChessPosition nextPosition = new ChessPosition(startingPosition.getRow() + place[0], startingPosition.getColumn() + place[1]);
            if (nextPosition.isOnBoard()) {
                if (isOccupied(nextPosition, pieceColor, validMoves, startingPosition, board)) {
                    if (nextPosition.getRow() == 8 || nextPosition.getRow() == 1) {
                        for (var pieceType : possiblePromotions) {
                            capture(nextPosition, pieceColor, validMoves, startingPosition, board, pieceType);
                        }
                    } else { capture(nextPosition, pieceColor, validMoves, startingPosition, board, null); }
                }
            }
        }

    }

    public static boolean isOccupied(ChessPosition nextPosition, ChessGame.TeamColor pieceColor, Collection<ChessMove> validMoves, ChessPosition startingPosition, ChessBoard board) {
        ChessPiece boardPiece = board.getPiece(nextPosition);
        if (boardPiece != null) { //nextPosition contains another piece
            return true;
        }
        return false;
    }

    public static void capture(ChessPosition nextPosition, ChessGame.TeamColor pieceColor, Collection<ChessMove> validMoves, ChessPosition startingPosition, ChessBoard board, ChessPiece.PieceType promotionPiece) {
        ChessPiece boardPiece = board.getPiece(nextPosition);
        if (boardPiece.getTeamColor() != pieceColor) { //add nextPosition if the piece is opposite team
            validMoves.add(new ChessMove(startingPosition, nextPosition, promotionPiece));
        }
    }
}
