package chess;

import java.util.HashSet;
import chess.PossibleMoves;

public class BishopMoveCalculator {

    public static HashSet<ChessMove> BishopMoves(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> givenHashSet) {
        ChessMove UpRight = PossibleMoves.UpRightMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove UpLeft = PossibleMoves.UpLeftMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove DownRight = PossibleMoves.DownRightMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove DownLeft = PossibleMoves.DownLeftMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);


        ChessPosition tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(UpRight != null){
            givenHashSet.add(UpRight);
            tempPosition = new ChessPosition(tempPosition.getRow()+1, tempPosition.getColumn()+1);
            UpRight = PossibleMoves.UpRightMethod(board, myPosition, tempPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        }
        tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(UpLeft != null){
            givenHashSet.add(UpLeft);
            tempPosition = new ChessPosition(tempPosition.getRow()+1, tempPosition.getColumn()-1);
            UpLeft = PossibleMoves.UpLeftMethod(board, myPosition, tempPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        }
        tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(DownRight != null){
            givenHashSet.add(DownRight);
            tempPosition = new ChessPosition(tempPosition.getRow()-1, tempPosition.getColumn()+1);
            DownRight = PossibleMoves.DownRightMethod(board, myPosition, tempPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        }
        tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(DownLeft != null){
            givenHashSet.add(DownLeft);
            tempPosition = new ChessPosition(tempPosition.getRow()-1, tempPosition.getColumn()-1);
            DownLeft = PossibleMoves.DownLeftMethod(board, myPosition, tempPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        }

        return givenHashSet;
    }
}