package chess;

import java.util.HashSet;

public class RookMoveCalculator {

    public static HashSet<ChessMove> RookMoves(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> givenHashSet) {
        ChessMove Up = PossibleMoves.UpMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove Down = PossibleMoves.DownMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove Right = PossibleMoves.RightMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove Left = PossibleMoves.LeftMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);


        ChessPosition tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(Up != null){
            givenHashSet.add(Up);
            tempPosition = new ChessPosition(tempPosition.getRow()+1, tempPosition.getColumn());
            Up = PossibleMoves.UpMethod(board, myPosition, tempPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        }
        tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(Down != null){
            givenHashSet.add(Down);
            tempPosition = new ChessPosition(tempPosition.getRow()-1, tempPosition.getColumn());
            Down = PossibleMoves.DownMethod(board, myPosition, tempPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        }
        tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(Right != null){
            givenHashSet.add(Right);
            tempPosition = new ChessPosition(tempPosition.getRow(), tempPosition.getColumn()+1);
            Right = PossibleMoves.RightMethod(board, myPosition, tempPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        }
        tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(Left != null){
            givenHashSet.add(Left);
            tempPosition = new ChessPosition(tempPosition.getRow(), tempPosition.getColumn()-1);
            Left = PossibleMoves.LeftMethod(board, myPosition, tempPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        }

        return givenHashSet;
    }
}
