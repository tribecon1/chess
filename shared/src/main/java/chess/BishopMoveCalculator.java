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

    public static ChessMove DownRightMethod(ChessBoard board, ChessPosition currPosition, ChessGame.TeamColor currColor) {
        ChessPosition DownRightPos = new ChessPosition(currPosition.getRow()-1, currPosition.getColumn()+1);
        if(DownRightPos.getRow() < 1 || DownRightPos.getColumn() > 8){
            return null;
        }
        else if (board.getPiece(DownRightPos) != null && board.getPiece(DownRightPos).getTeamColor() == currColor){
            return null;
        }
        else{
            return new ChessMove(currPosition, DownRightPos, ChessPiece.PieceType.BISHOP);
        }
    }

    public static ChessMove DownLeftMethod(ChessBoard board, ChessPosition currPosition, ChessGame.TeamColor currColor) {
        ChessPosition DownLeftPos = new ChessPosition(currPosition.getRow()-1, currPosition.getColumn()-1);
        if(DownLeftPos.getRow() < 1 || DownLeftPos.getColumn() < 1){
            return null;
        }
        else if (board.getPiece(DownLeftPos) != null && board.getPiece(DownLeftPos).getTeamColor() == currColor){
            return null;
        }
        else{
            return new ChessMove(currPosition, DownLeftPos, ChessPiece.PieceType.BISHOP);
        }
    }




    public static HashSet<ChessMove> BishopMoves(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> givenHashSet) {
        ChessMove UpRight = UpRightMethod(board, myPosition, board.getPiece(myPosition).getTeamColor());
        ChessMove UpLeft = UpLeftMethod(board, myPosition, board.getPiece(myPosition).getTeamColor());
        ChessMove DownRight = DownRightMethod(board, myPosition, board.getPiece(myPosition).getTeamColor());
        ChessMove DownLeft = DownLeftMethod(board, myPosition, board.getPiece(myPosition).getTeamColor());

        /*System.out.println(UpRight);
        System.out.println(UpLeft);
        System.out.println(DownRight);
        System.out.println(DownLeft);*/

        ChessPosition tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(UpRight != null){
            givenHashSet.add(UpRight);
            tempPosition = new ChessPosition(tempPosition.getRow()+1, tempPosition.getColumn()+1);
            UpRight = UpRightMethod(board, tempPosition, board.getPiece(myPosition).getTeamColor());
        }
        tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(UpLeft != null){
            givenHashSet.add(UpLeft);
            tempPosition = new ChessPosition(tempPosition.getRow()+1, tempPosition.getColumn()-1);
            UpLeft = UpLeftMethod(board, tempPosition, board.getPiece(myPosition).getTeamColor());
        }
        tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(DownRight != null){
            givenHashSet.add(DownRight);
            tempPosition = new ChessPosition(tempPosition.getRow()-1, tempPosition.getColumn()+1);
            DownRight = DownRightMethod(board, tempPosition, board.getPiece(myPosition).getTeamColor());
        }
        tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(DownLeft != null){
            givenHashSet.add(DownLeft);
            tempPosition = new ChessPosition(tempPosition.getRow()-1, tempPosition.getColumn()-1);
            DownLeft = DownLeftMethod(board, tempPosition, board.getPiece(myPosition).getTeamColor());
        }
        /*for (ChessMove move : givenHashSet) {
            System.out.println(move);
        }*/
        return givenHashSet;
    }
}