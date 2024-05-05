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

    public static ChessMove LForwardRightMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition LForwardRightPos = new ChessPosition(movePosition.getRow()+2, movePosition.getColumn()+1);
        if(LForwardRightPos.getRow() > 8 || LForwardRightPos.getColumn() > 8){
            return null;
        }
        else if (board.getPiece(LForwardRightPos) != null){
            if (DifferentTeam(board, LForwardRightPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, LForwardRightPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, LForwardRightPos, null);
        }
    }

    public static ChessMove LForwardLeftMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition LForwardLeftPos = new ChessPosition(movePosition.getRow()+2, movePosition.getColumn()-1);
        if(LForwardLeftPos.getRow() > 8 || LForwardLeftPos.getColumn() < 1){
            return null;
        }
        else if (board.getPiece(LForwardLeftPos) != null){
            if (DifferentTeam(board, LForwardLeftPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, LForwardLeftPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, LForwardLeftPos, null);
        }
    }

    public static ChessMove LRightUpMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition LRightUpPos = new ChessPosition(movePosition.getRow()+1, movePosition.getColumn()+2);
        if(LRightUpPos.getRow() > 8 || LRightUpPos.getColumn() > 8){
            return null;
        }
        else if (board.getPiece(LRightUpPos) != null){
            if (DifferentTeam(board, LRightUpPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, LRightUpPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, LRightUpPos, null);
        }
    }

    public static ChessMove LRightDownMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition LRightDownPos = new ChessPosition(movePosition.getRow()-1, movePosition.getColumn()+2);
        if(LRightDownPos.getRow() < 1 || LRightDownPos.getColumn() > 8){
            return null;
        }
        else if (board.getPiece(LRightDownPos) != null){
            if (DifferentTeam(board, LRightDownPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, LRightDownPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, LRightDownPos, null);
        }
    }

    public static ChessMove LBackwardRightMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition LBackwardRightPos = new ChessPosition(movePosition.getRow()-2, movePosition.getColumn()+1);
        if(LBackwardRightPos.getRow() < 1 || LBackwardRightPos.getColumn() > 8){
            return null;
        }
        else if (board.getPiece(LBackwardRightPos) != null){
            if (DifferentTeam(board, LBackwardRightPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, LBackwardRightPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, LBackwardRightPos, null);
        }
    }

    public static ChessMove LBackwardLeftMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition LBackwardLeftPos = new ChessPosition(movePosition.getRow()-2, movePosition.getColumn()-1);
        if(LBackwardLeftPos.getRow() < 1 || LBackwardLeftPos.getColumn() < 1){
            return null;
        }
        else if (board.getPiece(LBackwardLeftPos) != null){
            if (DifferentTeam(board, LBackwardLeftPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, LBackwardLeftPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, LBackwardLeftPos, null);
        }
    }

    public static ChessMove LLeftUpMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition LLeftUpPos = new ChessPosition(movePosition.getRow()+1, movePosition.getColumn()-2);
        if(LLeftUpPos.getRow() > 8 || LLeftUpPos.getColumn() < 1){
            return null;
        }
        else if (board.getPiece(LLeftUpPos) != null){
            if (DifferentTeam(board, LLeftUpPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, LLeftUpPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, LLeftUpPos, null);
        }
    }

    public static ChessMove LLeftDownMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition LLeftDownPos = new ChessPosition(movePosition.getRow()-1, movePosition.getColumn()-2);
        if(LLeftDownPos.getRow() < 1 || LLeftDownPos.getColumn() < 1){
            return null;
        }
        else if (board.getPiece(LLeftDownPos) != null){
            if (DifferentTeam(board, LLeftDownPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, LLeftDownPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, LLeftDownPos, null);
        }
    }







}
