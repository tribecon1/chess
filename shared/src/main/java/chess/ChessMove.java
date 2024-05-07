package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private final ChessPosition start;
    private final ChessPosition end;
    private final ChessPiece.PieceType currPiece;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessMove chessMove)) return false;
        return Objects.equals(start, chessMove.start) && Objects.equals(end, chessMove.end) && currPiece == chessMove.currPiece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, currPiece);
    }

    @Override
    public String toString() {
        return "ChessMove(Start: " + start + ", End: " + end + ", Piece: " + currPiece + ")";
    }


    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.start = startPosition;
        this.end = endPosition;
        this.currPiece = promotionPiece;

    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return start;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return end;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        if (currPiece == ChessPiece.PieceType.PAWN && (end.getRow() == 1 || end.getRow() == 8)){
            return ChessPiece.PieceType.PAWN;//HOW TO HANDLE COLOR, IF NECESSARY? WHEN NEEDED??
        }
        else{
            return null;
        }
    }
}
