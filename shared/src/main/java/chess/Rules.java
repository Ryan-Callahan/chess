package chess;

import java.util.Collection;

public class Rules {

    public static void possibleMoves (ChessPiece piece, ChessBoard board, ChessPosition startPosition, Collection<ChessMove> moves) {
        switch (piece.getPieceType()) {
            case KING:
                int[][] kingDirections = {{-1, -1}, {1, -1}, {1, 1}, {-1, 1}, {0, -1}, {1, 0}, {0, 1}, {-1, 0}};
                singleMove(kingDirections, piece, board, startPosition, moves);
                break;
            case QUEEN:
                int[][] queenDirections = {{-1, -1}, {1, -1}, {1, 1}, {-1, 1}, {0, -1}, {1, 0}, {0, 1}, {-1, 0}};
                multiMove(queenDirections, piece, board, startPosition, moves);
                break;
            case BISHOP:
                int[][] bishopDirections = {{-1, -1}, {1, -1}, {1, 1}, {-1, 1}};
                multiMove(bishopDirections, piece, board, startPosition, moves);
                break;
            case KNIGHT:
                int[][] knightDirections = {{2, -1}, {2, 1}, {1, -2}, {1, 2}, {-1, -2}, {-1, 2}, {-2, -1}, {-2, 1}};
                singleMove(knightDirections, piece, board, startPosition, moves);
                break;
            case ROOK:
                int[][] rookDirections = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
                multiMove(rookDirections, piece, board, startPosition, moves);
                break;
            case PAWN:
                pawnMove(piece, board, startPosition, moves);
                break;
        }
    }

    private static void multiMove (int[][] directions, ChessPiece piece, ChessBoard board, ChessPosition startPosition, Collection<ChessMove> moves) {
        for (int[] direction : directions) {
            ChessPosition nextPosition = new ChessPosition(startPosition.getRow()+direction[0], startPosition.getColumn()+direction[1]);
            while (nextPosition.isOnBoard()) {
                if (isOccupied(board, nextPosition)) {
                    captureIfCapturable(piece, board, startPosition, nextPosition, moves, null);
                    break;
                } else {
                    moves.add(new ChessMove(startPosition, nextPosition, null));
                }
                nextPosition = new ChessPosition(nextPosition.getRow()+direction[0], nextPosition.getColumn()+direction[1]);
            }
        }
    }

    private static void singleMove (int[][] directions, ChessPiece piece, ChessBoard board, ChessPosition startPosition, Collection<ChessMove> moves) {
        for (int[] direction : directions) {
            ChessPosition nextPosition = new ChessPosition(startPosition.getRow()+direction[0], startPosition.getColumn()+direction[1]);
            if (nextPosition.isOnBoard()) {
                if (isOccupied(board, nextPosition)) {
                    if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                        if (direction[1] != 0) {
                            pawnAction(piece, board, startPosition, nextPosition, moves, true);
                        }
                    } else {
                        captureIfCapturable(piece, board, startPosition, nextPosition, moves, null);
                    }
                } else {
                    if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                        if (direction[1] == 0) {
                            pawnAction(piece, board, startPosition, nextPosition, moves, false);
                        }
                    } else {
                        moves.add(new ChessMove(startPosition, nextPosition, null));
                    }
                }
            }
        }
    }

    private static void pawnMove (ChessPiece piece, ChessBoard board, ChessPosition startPosition, Collection<ChessMove> moves) {
        int vector = 1;
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            vector = -1;
        }

        if (((piece.getTeamColor() == ChessGame.TeamColor.BLACK && startPosition.getRow() == 7)
                || (piece.getTeamColor() == ChessGame.TeamColor.WHITE && startPosition.getRow() == 2))
                && !isOccupied(board, new ChessPosition(startPosition.getRow()+vector, startPosition.getColumn()))) {
            int[][] enPassant = {{vector, 0}, {vector*2, 0}};
            singleMove(enPassant, piece, board, startPosition, moves);
        } else {
            int[][] forwards = {{vector, 0}};
            singleMove(forwards, piece, board, startPosition, moves);
        }

        int[][] diagonals = {{vector, -1}, {vector, 1}};
        singleMove(diagonals, piece, board, startPosition, moves);
    }

    private static boolean isOccupied (ChessBoard board, ChessPosition position) {
        return board.getPiece(position) != null;
    }

    private static void captureIfCapturable (ChessPiece piece, ChessBoard board, ChessPosition startPosition, ChessPosition nextPosition, Collection<ChessMove> moves, ChessPiece.PieceType promotionPiece) {
        if (board.getPiece(nextPosition).getTeamColor() != piece.getTeamColor()) {
            moves.add(new ChessMove(startPosition, nextPosition, promotionPiece));
        }
    }

    private static void pawnAction (ChessPiece piece, ChessBoard board, ChessPosition startPosition, ChessPosition nextPosition, Collection<ChessMove> moves, boolean capture) {
        if ((piece.getTeamColor() == ChessGame.TeamColor.BLACK && nextPosition.getRow() == 1)
                || (piece.getTeamColor() == ChessGame.TeamColor.WHITE && nextPosition.getRow() == 8)) {
            ChessPiece.PieceType[] possiblePromotions = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT};
            for (var promotion : possiblePromotions) {
                if (capture) {
                    captureIfCapturable(piece, board, startPosition, nextPosition, moves, promotion);
                } else {
                    moves.add(new ChessMove(startPosition, nextPosition, promotion));
                }
            }
        } else {
            if (capture) {
                captureIfCapturable(piece, board, startPosition, nextPosition, moves, null);
            } else {
                moves.add(new ChessMove(startPosition, nextPosition, null));
            }
        }
    }
}
