package chess;

import java.util.HashSet;

public class PossibleMoves {
    public static boolean DifferentTeam(ChessBoard board, ChessPosition checkedPos, ChessGame.TeamColor currColor) {
        return board.getPiece(checkedPos).getTeamColor() != currColor;
    }


    public static ChessMove UpRightMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition upRightPos = new ChessPosition(movePosition.getRow()+1, movePosition.getColumn()+1);
        if(upRightPos.getRow() > 8 || upRightPos.getColumn() > 8){
            return null;
        }
        else if (board.getPiece(upRightPos) != null){
            if (DifferentTeam(board, upRightPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, upRightPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, upRightPos, null);
        }
    }

    public static ChessMove UpLeftMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition upLeftPos = new ChessPosition(movePosition.getRow()+1, movePosition.getColumn()-1);
        if(upLeftPos.getRow() > 8 || upLeftPos.getColumn() < 1){
            return null;
        }
        else if (board.getPiece(upLeftPos) != null){
            if (DifferentTeam(board, upLeftPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, upLeftPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, upLeftPos, null);
        }
    }

    public static ChessMove DownRightMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition downRightPos = new ChessPosition(movePosition.getRow()-1, movePosition.getColumn()+1);
        if(downRightPos.getRow() < 1 || downRightPos.getColumn() > 8){
            return null;
        }
        else if (board.getPiece(downRightPos) != null){
            if (DifferentTeam(board, downRightPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, downRightPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, downRightPos, null);
        }
    }

    public static ChessMove DownLeftMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition downLeftPos = new ChessPosition(movePosition.getRow()-1, movePosition.getColumn()-1);
        if(downLeftPos.getRow() < 1 || downLeftPos.getColumn() < 1){
            return null;
        }
        else if (board.getPiece(downLeftPos) != null){
            if (DifferentTeam(board, downLeftPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, downLeftPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, downLeftPos, null);
        }
    }

    public static ChessMove UpMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition upPos = new ChessPosition(movePosition.getRow()+1, movePosition.getColumn());
        if(upPos.getRow() > 8){
            return null;
        }
        else if (board.getPiece(upPos) != null){
            if (DifferentTeam(board, upPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, upPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, upPos, null);
        }
    }

    public static ChessMove DownMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition downPos = new ChessPosition(movePosition.getRow()-1, movePosition.getColumn());
        if (downPos.getRow() < 1) {
            return null;
        }
        else if (board.getPiece(downPos) != null) {
            if (DifferentTeam(board, downPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, downPos, null));
            }
            return null;
        }
        else {
            return new ChessMove(startPosition, downPos, null);
        }
    }

    public static ChessMove RightMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition rightPos = new ChessPosition(movePosition.getRow(), movePosition.getColumn()+1);
        if (rightPos.getColumn() > 8) {
            return null;
        }
        else if (board.getPiece(rightPos) != null) {
            if (DifferentTeam(board, rightPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, rightPos, null));
            }
            return null;
        }
        else {
            return new ChessMove(startPosition, rightPos, null);
        }
    }

    public static ChessMove LeftMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition leftPos = new ChessPosition(movePosition.getRow(), movePosition.getColumn()-1);
        if (leftPos.getColumn() < 1) {
            return null;
        }
        else if (board.getPiece(leftPos) != null) {
            if (DifferentTeam(board, leftPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, leftPos, null));
            }
            return null;
        }
        else {
            return new ChessMove(startPosition, leftPos, null);
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
