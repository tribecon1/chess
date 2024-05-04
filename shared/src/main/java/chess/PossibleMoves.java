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

    public static ChessMove UpMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition UpPos = new ChessPosition(movePosition.getRow()+1, movePosition.getColumn());
        if(UpPos.getRow() > 8){
            return null;
        }
        else if (board.getPiece(UpPos) != null){
            if (DifferentTeam(board, UpPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, UpPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, UpPos, null);
        }
    }

    public static ChessMove DownMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition DownPos = new ChessPosition(movePosition.getRow()-1, movePosition.getColumn());
        if (DownPos.getRow() < 1) {
            return null;
        }
        else if (board.getPiece(DownPos) != null) {
            if (DifferentTeam(board, DownPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, DownPos, null));
            }
            return null;
        }
        else {
            return new ChessMove(startPosition, DownPos, null);
        }
    }

    public static ChessMove RightMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition RightPos = new ChessPosition(movePosition.getRow(), movePosition.getColumn()+1);
        if (RightPos.getColumn() > 8) {
            return null;
        }
        else if (board.getPiece(RightPos) != null) {
            if (DifferentTeam(board, RightPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, RightPos, null));
            }
            return null;
        }
        else {
            return new ChessMove(startPosition, RightPos, null);
        }
    }

    public static ChessMove LeftMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition LeftPos = new ChessPosition(movePosition.getRow(), movePosition.getColumn()-1);
        if (LeftPos.getColumn() < 1) {
            return null;
        }
        else if (board.getPiece(LeftPos) != null) {
            if (DifferentTeam(board, LeftPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, LeftPos, null));
            }
            return null;
        }
        else {
            return new ChessMove(startPosition, LeftPos, null);
        }
    }

}
