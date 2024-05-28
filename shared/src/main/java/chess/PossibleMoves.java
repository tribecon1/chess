package chess;

import java.util.Collection;
import java.util.HashSet;

public class PossibleMoves {
    public static boolean differentTeam(ChessBoard board, ChessPosition checkedPos, ChessGame.TeamColor currColor) {
        return board.getPiece(checkedPos).getTeamColor() != currColor;
    }

    public static boolean blankSpaceCastlingChecker (ChessGame.TeamColor team, ChessPosition rookPos, ChessBoard currBoard) {
        if (team == ChessGame.TeamColor.WHITE) {
            if (rookPos.getColumn() == 1){
                for (int j = 2; j < 5; j++){
                    if (currBoard.getPiece(new ChessPosition(1,j)) != null){
                        return false;
                    }
                }
                return true;
            }
            else if (rookPos.getColumn() == 8){
                for (int j = 6; j < 8; j++){
                    if (currBoard.getPiece(new ChessPosition(1,j)) != null){
                        return false;
                    }
                }
                return true;
            }
        }
        else{
            if (rookPos.getColumn() == 1){
                for (int j = 2; j < 5; j++){
                    if (currBoard.getPiece(new ChessPosition(8,j)) != null){
                        return false;
                    }
                }
                return true;
            }
            else if (rookPos.getColumn() == 8){
                for (int j = 6; j < 8; j++){
                    if (currBoard.getPiece(new ChessPosition(8,j)) != null){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }


    public static boolean wouldLandOnThisPiece (ChessPosition posOfPieceInDanger, ChessBoard givenBoard, ChessGame.TeamColor currTeam) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (givenBoard.getPiece(new ChessPosition(i, j)) != null && givenBoard.getPiece(new ChessPosition(i, j)).getTeamColor() != currTeam) {
                    ChessPosition opponentPosition = new ChessPosition(i, j);
                    ChessPiece opponentPiece = givenBoard.getPiece(opponentPosition);
                    Collection<ChessMove> movesProvided = opponentPiece.pieceMoves(givenBoard, opponentPosition);
                    for (ChessMove potentialMove : movesProvided) {
                        if (potentialMove.getEndPosition().equals(posOfPieceInDanger)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    public static ChessMove upRightMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition upRightPos = new ChessPosition(movePosition.getRow()+1, movePosition.getColumn()+1);
        if(upRightPos.getRow() > 8 || upRightPos.getColumn() > 8){
            return null;
        }
        else if (board.getPiece(upRightPos) != null){
            if (differentTeam(board, upRightPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, upRightPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, upRightPos, null);
        }
    }

    public static ChessMove upLeftMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition upLeftPos = new ChessPosition(movePosition.getRow()+1, movePosition.getColumn()-1);
        if(upLeftPos.getRow() > 8 || upLeftPos.getColumn() < 1){
            return null;
        }
        else if (board.getPiece(upLeftPos) != null){
            if (differentTeam(board, upLeftPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, upLeftPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, upLeftPos, null);
        }
    }

    public static ChessMove downRightMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition downRightPos = new ChessPosition(movePosition.getRow()-1, movePosition.getColumn()+1);
        if(downRightPos.getRow() < 1 || downRightPos.getColumn() > 8){
            return null;
        }
        else if (board.getPiece(downRightPos) != null){
            if (differentTeam(board, downRightPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, downRightPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, downRightPos, null);
        }
    }

    public static ChessMove downLeftMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition downLeftPos = new ChessPosition(movePosition.getRow()-1, movePosition.getColumn()-1);
        if(downLeftPos.getRow() < 1 || downLeftPos.getColumn() < 1){
            return null;
        }
        else if (board.getPiece(downLeftPos) != null){
            if (differentTeam(board, downLeftPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, downLeftPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, downLeftPos, null);
        }
    }

    public static ChessMove upMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition upPos = new ChessPosition(movePosition.getRow()+1, movePosition.getColumn());
        if(upPos.getRow() > 8){
            return null;
        }
        else if (board.getPiece(upPos) != null){
            if (differentTeam(board, upPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, upPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, upPos, null);
        }
    }

    public static ChessMove downMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition downPos = new ChessPosition(movePosition.getRow()-1, movePosition.getColumn());
        if (downPos.getRow() < 1) {
            return null;
        }
        else if (board.getPiece(downPos) != null) {
            if (differentTeam(board, downPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, downPos, null));
            }
            return null;
        }
        else {
            return new ChessMove(startPosition, downPos, null);
        }
    }

    public static ChessMove rightMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition rightPos = new ChessPosition(movePosition.getRow(), movePosition.getColumn()+1);
        if (rightPos.getColumn() > 8) {
            return null;
        }
        else if (board.getPiece(rightPos) != null) {
            if (differentTeam(board, rightPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, rightPos, null));
            }
            return null;
        }
        else {
            return new ChessMove(startPosition, rightPos, null);
        }
    }

    public static ChessMove leftMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition leftPos = new ChessPosition(movePosition.getRow(), movePosition.getColumn()-1);
        if (leftPos.getColumn() < 1) {
            return null;
        }
        else if (board.getPiece(leftPos) != null) {
            if (differentTeam(board, leftPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, leftPos, null));
            }
            return null;
        }
        else {
            return new ChessMove(startPosition, leftPos, null);
        }
    }

    public static ChessMove forwardRightLMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition forwardRightLPos = new ChessPosition(movePosition.getRow()+2, movePosition.getColumn()+1);
        if(forwardRightLPos.getRow() > 8 || forwardRightLPos.getColumn() > 8){
            return null;
        }
        else if (board.getPiece(forwardRightLPos) != null){
            if (differentTeam(board, forwardRightLPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, forwardRightLPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, forwardRightLPos, null);
        }
    }

    public static ChessMove forwardLeftLMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition forwardLeftLPos = new ChessPosition(movePosition.getRow()+2, movePosition.getColumn()-1);
        if(forwardLeftLPos.getRow() > 8 || forwardLeftLPos.getColumn() < 1){
            return null;
        }
        else if (board.getPiece(forwardLeftLPos) != null){
            if (differentTeam(board, forwardLeftLPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, forwardLeftLPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, forwardLeftLPos, null);
        }
    }

    public static ChessMove rightUpLMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition rightUpLPos = new ChessPosition(movePosition.getRow()+1, movePosition.getColumn()+2);
        if(rightUpLPos.getRow() > 8 || rightUpLPos.getColumn() > 8){
            return null;
        }
        else if (board.getPiece(rightUpLPos) != null){
            if (differentTeam(board, rightUpLPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, rightUpLPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, rightUpLPos, null);
        }
    }

    public static ChessMove rightDownLMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition rightDownLPos = new ChessPosition(movePosition.getRow()-1, movePosition.getColumn()+2);
        if(rightDownLPos.getRow() < 1 || rightDownLPos.getColumn() > 8){
            return null;
        }
        else if (board.getPiece(rightDownLPos) != null){
            if (differentTeam(board, rightDownLPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, rightDownLPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, rightDownLPos, null);
        }
    }

    public static ChessMove backwardRightLMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition backwardRightLPos = new ChessPosition(movePosition.getRow()-2, movePosition.getColumn()+1);
        if(backwardRightLPos.getRow() < 1 || backwardRightLPos.getColumn() > 8){
            return null;
        }
        else if (board.getPiece(backwardRightLPos) != null){
            if (differentTeam(board, backwardRightLPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, backwardRightLPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, backwardRightLPos, null);
        }
    }

    public static ChessMove backwardLeftLMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition backwardLeftLPos = new ChessPosition(movePosition.getRow()-2, movePosition.getColumn()-1);
        if(backwardLeftLPos.getRow() < 1 || backwardLeftLPos.getColumn() < 1){
            return null;
        }
        else if (board.getPiece(backwardLeftLPos) != null){
            if (differentTeam(board, backwardLeftLPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, backwardLeftLPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, backwardLeftLPos, null);
        }
    }

    public static ChessMove leftUpLMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition leftUpLPos = new ChessPosition(movePosition.getRow()+1, movePosition.getColumn()-2);
        if(leftUpLPos.getRow() > 8 || leftUpLPos.getColumn() < 1){
            return null;
        }
        else if (board.getPiece(leftUpLPos) != null){
            if (differentTeam(board, leftUpLPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, leftUpLPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, leftUpLPos, null);
        }
    }

    public static ChessMove leftDownLMethod(ChessBoard board, ChessPosition startPosition, ChessPosition movePosition, ChessGame.TeamColor currColor, HashSet<ChessMove> givenHashSet) {
        ChessPosition leftDownLPos = new ChessPosition(movePosition.getRow()-1, movePosition.getColumn()-2);
        if(leftDownLPos.getRow() < 1 || leftDownLPos.getColumn() < 1){
            return null;
        }
        else if (board.getPiece(leftDownLPos) != null){
            if (differentTeam(board, leftDownLPos, currColor)) {
                givenHashSet.add(new ChessMove(startPosition, leftDownLPos, null));
            }
            return null;
        }
        else{
            return new ChessMove(startPosition, leftDownLPos, null);
        }
    }

}
