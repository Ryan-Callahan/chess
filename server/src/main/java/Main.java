import chess.*;
import server.Server;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        var server = new Server();
        server.run(Integer.parseInt(args[0]));
        System.out.println("♕ 240 Chess Server: " + piece);
    }
}