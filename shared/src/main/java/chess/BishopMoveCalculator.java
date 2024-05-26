package chess;

import java.util.HashSet;

public class BishopMoveCalculator {

    public static HashSet<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> givenHashSet) {
        ChessMove upRight = PossibleMoves.upRightMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove upLeft = PossibleMoves.upLeftMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove downRight = PossibleMoves.downRightMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove downLeft = PossibleMoves.downLeftMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);


        ChessPosition tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(upRight != null){
            givenHashSet.add(upRight);
            tempPosition = new ChessPosition(tempPosition.getRow()+1, tempPosition.getColumn()+1);
            upRight = PossibleMoves.upRightMethod(board, myPosition, tempPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        }
        tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(upLeft != null){
            givenHashSet.add(upLeft);
            tempPosition = new ChessPosition(tempPosition.getRow()+1, tempPosition.getColumn()-1);
            upLeft = PossibleMoves.upLeftMethod(board, myPosition, tempPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        }
        tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(downRight != null){
            givenHashSet.add(downRight);
            tempPosition = new ChessPosition(tempPosition.getRow()-1, tempPosition.getColumn()+1);
            downRight = PossibleMoves.downRightMethod(board, myPosition, tempPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        }
        tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(downLeft != null){
            givenHashSet.add(downLeft);
            tempPosition = new ChessPosition(tempPosition.getRow()-1, tempPosition.getColumn()-1);
            downLeft = PossibleMoves.downLeftMethod(board, myPosition, tempPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        }

        return givenHashSet;
    }
}