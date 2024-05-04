package chess;

import java.util.HashSet;

public class BishopMoveCalculator {

    public static ChessMove UpRightMethod(ChessBoard board, ChessPosition currPosition, ChessGame.TeamColor currColor) {
        ChessPosition UpRightPos = new ChessPosition(currPosition.getRow()+1, currPosition.getColumn()+1);
        if(UpRightPos.getRow() > 8 || UpRightPos.getColumn() > 8){
            return null;
        }
        else if (board.getPiece(UpRightPos) != null && board.getPiece(UpRightPos).getTeamColor() == currColor){
            return null;
        }
        else{
            return new ChessMove(currPosition, UpRightPos, ChessPiece.PieceType.BISHOP);
        }
    }

    public static ChessMove UpLeftMethod(ChessBoard board, ChessPosition currPosition, ChessGame.TeamColor currColor) {
        ChessPosition UpLeftPos = new ChessPosition(currPosition.getRow()+1, currPosition.getColumn()-1);
        if(UpLeftPos.getRow() > 8 || UpLeftPos.getColumn() < 1){
            return null;
        }
        else if (board.getPiece(UpLeftPos) != null && board.getPiece(UpLeftPos).getTeamColor() == currColor){
            return null;
        }
        else{
            return new ChessMove(currPosition, UpLeftPos, ChessPiece.PieceType.BISHOP);
        }
    }


    public static HashSet<ChessMove> BishopMoves(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> givenHashSet) {
        ChessMove UpRight = UpRightMethod(board, myPosition, board.getPiece(myPosition).getTeamColor());
        while(UpRight != null){
            givenHashSet.add(UpRight);
            ChessPosition tempPosition = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
            UpRight = UpRightMethod(board, tempPosition, board.getPiece(myPosition).getTeamColor());
        }




        return givenHashSet;
    }
}