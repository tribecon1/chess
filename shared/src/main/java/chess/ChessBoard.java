package chess;

import java.util.Arrays;
import java.util.Objects;
import chess.ChessGame.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] board;

    public ChessBoard() {
        //create 2D array of 8x8
        board = new ChessPiece[8][8]; //a 8x8 2D array that is built to hold ChessPiece objs. or null
    }


    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if(position == null || piece == null){
            //making sure nothing passed in as anything BUT a ChessPosition or -Piece object
            throw new IllegalArgumentException();
        }
        board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    public void removePiece(ChessPosition position) {
        if (position == null){
            throw new IllegalArgumentException();
        }
        else {
            board[position.getRow()-1][position.getColumn()-1] = null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessBoard that)) return false;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        //if (ChessBoard) call getPosition, check array at this position if equals a ChessPiece obj.
        if (position.getRow() < 1 || position.getColumn() < 1 || position.getRow() > 8 || position.getColumn() > 8){
            return null;
        } //trying this out!
        if (board[position.getRow()-1][position.getColumn()-1] != null){
            return board[position.getRow()-1][position.getColumn()-1];
        }
        return null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //resets board to blank
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }
        //White Rook represents (0,0) or Row 1, Column 1 in the chessboard!
        for (int col = 0; col < 8; col++) {//inserting black and white pawns in 2nd and 7th rows
            board[1][col] = new ChessPiece(TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            board[6][col] = new ChessPiece(TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
        int[] rookIndices = {0,7};
        for (int index : rookIndices){
            board[0][index] = new ChessPiece(TeamColor.WHITE, ChessPiece.PieceType.ROOK);
            board[7][index] = new ChessPiece(TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        }
        int[] knightIndices = {1,6};
        for (int index : knightIndices){
            board[0][index] = new ChessPiece(TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
            board[7][index] = new ChessPiece(TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        }
        int[] bishopIndices = {2,5};
        for (int index : bishopIndices){
            board[0][index] = new ChessPiece(TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
            board[7][index] = new ChessPiece(TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        }
        //Queen Pieces set
        board[0][3] = new ChessPiece(TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        board[7][3] = new ChessPiece(TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        //King Pieces set
        board[0][4] = new ChessPiece(TeamColor.WHITE, ChessPiece.PieceType.KING);
        board[7][4] = new ChessPiece(TeamColor.BLACK, ChessPiece.PieceType.KING);
    }
}
