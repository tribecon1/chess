package chess;

import java.util.HashSet;

public class PossibleMoves {
    public static boolean DifferentTeam(ChessBoard board, ChessPosition checkedPos, ChessGame.TeamColor currColor) {
        return board.getPiece(checkedPos).getTeamColor() != currColor;
    }


    public static ChessMove UpRightMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition UpRightPos = new ChessPosition(movePosition.getRow()+1, movePosition.getColumn()+1);
        if(UpRightPos.getRow() > 8 || UpRightPos.getColumn() > 8){
            return null;
        }
        else if (board.getPiece(UpRightPos) != null){
            if (DifferentTeam(board, UpRightPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, UpRightPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, UpRightPos, null);
        }
    }

    public static ChessMove UpLeftMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition UpLeftPos = new ChessPosition(movePosition.getRow()+1, movePosition.getColumn()-1);
        if(UpLeftPos.getRow() > 8 || UpLeftPos.getColumn() < 1){
            return null;
        }
        else if (board.getPiece(UpLeftPos) != null){
            if (DifferentTeam(board, UpLeftPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, UpLeftPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, UpLeftPos, null);
        }
    }

    public static ChessMove DownRightMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition DownRightPos = new ChessPosition(movePosition.getRow()-1, movePosition.getColumn()+1);
        if(DownRightPos.getRow() < 1 || DownRightPos.getColumn() > 8){
            return null;
        }
        else if (board.getPiece(DownRightPos) != null){
            if (DifferentTeam(board, DownRightPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, DownRightPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, DownRightPos, null);
        }
    }

    public static ChessMove DownLeftMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition DownLeftPos = new ChessPosition(movePosition.getRow()-1, movePosition.getColumn()-1);
        if(DownLeftPos.getRow() < 1 || DownLeftPos.getColumn() < 1){
            return null;
        }
        else if (board.getPiece(DownLeftPos) != null){
            if (DifferentTeam(board, DownLeftPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, DownLeftPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, DownLeftPos, null);
        }
    }




}
