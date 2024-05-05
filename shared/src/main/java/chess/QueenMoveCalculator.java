package chess;

import java.util.HashSet;

public class QueenMoveCalculator {

    public static HashSet<ChessMove> QueenMoves(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> givenHashSet) {
        ChessMove UpRight = PossibleMoves.UpRightMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove UpLeft = PossibleMoves.UpLeftMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove DownRight = PossibleMoves.DownRightMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove DownLeft = PossibleMoves.DownLeftMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove Up = PossibleMoves.UpMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove Down = PossibleMoves.DownMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove Right = PossibleMoves.RightMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        ChessMove Left = PossibleMoves.LeftMethod(board, myPosition, myPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);


        ChessPosition tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while (UpRight != null) {
            givenHashSet.add(UpRight);
            tempPosition = new ChessPosition(tempPosition.getRow() + 1, tempPosition.getColumn() + 1);
            UpRight = PossibleMoves.UpRightMethod(board, myPosition, tempPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        }
        tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while (UpLeft != null) {
            givenHashSet.add(UpLeft);
            tempPosition = new ChessPosition(tempPosition.getRow() + 1, tempPosition.getColumn() - 1);
            UpLeft = PossibleMoves.UpLeftMethod(board, myPosition, tempPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        }
        tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while (DownRight != null) {
            givenHashSet.add(DownRight);
            tempPosition = new ChessPosition(tempPosition.getRow() - 1, tempPosition.getColumn() + 1);
            DownRight = PossibleMoves.DownRightMethod(board, myPosition, tempPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        }
        tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while (DownLeft != null) {
            givenHashSet.add(DownLeft);
            tempPosition = new ChessPosition(tempPosition.getRow() - 1, tempPosition.getColumn() - 1);
            DownLeft = PossibleMoves.DownLeftMethod(board, myPosition, tempPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        }

        tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while (Up != null) {
            givenHashSet.add(Up);
            tempPosition = new ChessPosition(tempPosition.getRow() + 1, tempPosition.getColumn());
            Up = PossibleMoves.UpMethod(board, myPosition, tempPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        }
        tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while (Down != null) {
            givenHashSet.add(Down);
            tempPosition = new ChessPosition(tempPosition.getRow() - 1, tempPosition.getColumn());
            Down = PossibleMoves.DownMethod(board, myPosition, tempPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        }
        tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while (Right != null) {
            givenHashSet.add(Right);
            tempPosition = new ChessPosition(tempPosition.getRow(), tempPosition.getColumn() + 1);
            Right = PossibleMoves.RightMethod(board, myPosition, tempPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        }
        tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while (Left != null) {
            givenHashSet.add(Left);
            tempPosition = new ChessPosition(tempPosition.getRow(), tempPosition.getColumn() - 1);
            Left = PossibleMoves.LeftMethod(board, myPosition, tempPosition, board.getPiece(myPosition).getTeamColor(), givenHashSet);
        }

        return givenHashSet;
    }
}
