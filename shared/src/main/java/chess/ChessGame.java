package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor thisTeamsTurn;
    private ChessBoard board;
    private static Collection<ChessMove> validMoves;

    private static boolean WinCheck; //WHITE in Check
    private static boolean BinCheck;

    private static boolean WinCheckmate;
    private static boolean BinCheckmate;

    private static boolean WinStalemate;
    private static boolean BinStalemate;

    private ChessPosition WKingPos;
    private ChessPosition BKingPos;




    public ChessGame() {
        this.board = new ChessBoard();
        this.thisTeamsTurn = TeamColor.WHITE; //White always starts in Chess
        this.WKingPos = new ChessPosition(1,5);
        this.BKingPos = new ChessPosition(8,5); //modified in MakeMove if King is moving
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.thisTeamsTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.thisTeamsTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) throws CloneNotSupportedException{
        ChessPiece currPiece = board.getPiece(startPosition);
        if (currPiece == null) {
            return null;
        }
        else{
            ChessBoard clonedBoard = (ChessBoard) this.board.clone();
            validMoves = currPiece.pieceMoves(board, startPosition);

            if (currPiece.getPieceType() == ChessPiece.PieceType.KING){
                //to see if any moves of King endanger itself by OTHER TEAM and are not valid
            }
            else{ //to see if any moves of a piece endanger the King of its OWN TEAM and are not valid
                for (ChessMove move : validMoves) {
                    clonedBoard.removePiece(move.getStartPosition());
                    clonedBoard.addPiece(move.getEndPosition(), currPiece);

                }
            }
            //for each move in validMoves, check USING CLONED BOARD if: a piece checks King
            //OR
            //King would put itself in Check if king is moving.
            //check for each move

            //calculate check, checkmate, king undefended etc. ie. look down a straight line, down a diagonal, etc.
            //do while loop, while col < 8, go up, in while loop check if rook, for while loop of diagonal, check pawn, etc.
            //ignore the piece of the king's team
            //FINISH!!
            return validMoves; //make sure will be reset to match piece each time

        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        boolean addedSuccess = validMoves.add(move);
        ChessPiece pieceBeingMoved = this.board.getPiece(move.getStartPosition());
        if(addedSuccess){ //any valid move should already be in Valid moves, and return false
            throw new InvalidMoveException();
        }
        else if(board.getPiece(move.getStartPosition()).getTeamColor() != thisTeamsTurn){ //not player's turn
            throw new InvalidMoveException();
        }
        else if(pieceBeingMoved.getPieceType() == ChessPiece.PieceType.KING){
            switch (pieceBeingMoved.getTeamColor()){
                case WHITE:
                    this.board.addPiece(move.getEndPosition(), pieceBeingMoved);
                    this.board.removePiece(move.getStartPosition());
                    WKingPos = move.getEndPosition();
                case BLACK:
                    this.board.addPiece(move.getEndPosition(), pieceBeingMoved);
                    this.board.removePiece(move.getStartPosition());
                    BKingPos = move.getEndPosition();
            }
        }
        else{
            this.board.addPiece(move.getEndPosition(), pieceBeingMoved);
            this.board.removePiece(move.getStartPosition());
            //delete ChessPiece object at start pos of ChessMove in ChessBoard array, set to null.
            //Then set reference at end pos of ChessMove in ChessBoard array
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return switch(teamColor){
            case WHITE -> WinCheck;
            case BLACK -> BinCheck;
        };    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return switch(teamColor){
            case WHITE -> WinCheckmate;
            case BLACK -> BinCheckmate;
        };
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return switch(teamColor){
            case WHITE -> WinStalemate;
            case BLACK -> BinStalemate;
        };    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
