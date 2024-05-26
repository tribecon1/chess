package chess;

import java.util.HashSet;

public class RookMoveCalculator {

    public static HashSet<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> givenHashSet) {
        ChessMove up = PossibleMoves.upMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove down = PossibleMoves.downMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove right = PossibleMoves.rightMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove left = PossibleMoves.leftMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);


        ChessPosition tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(up != null){
            givenHashSet.add(up);
            tempPosition = new ChessPosition(tempPosition.getRow()+1, tempPosition.getColumn());
            up = PossibleMoves.upMethod(board, myPosition, tempPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        }
        tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(down != null){
            givenHashSet.add(down);
            tempPosition = new ChessPosition(tempPosition.getRow()-1, tempPosition.getColumn());
            down = PossibleMoves.downMethod(board, myPosition, tempPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        }
        tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(right != null){
            givenHashSet.add(right);
            tempPosition = new ChessPosition(tempPosition.getRow(), tempPosition.getColumn()+1);
            right = PossibleMoves.rightMethod(board, myPosition, tempPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        }
        tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(left != null){
            givenHashSet.add(left);
            tempPosition = new ChessPosition(tempPosition.getRow(), tempPosition.getColumn()-1);
            left = PossibleMoves.leftMethod(board, myPosition, tempPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        }

        return givenHashSet;
    }
}
