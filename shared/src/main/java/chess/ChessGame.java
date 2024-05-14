package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor thisTeamsTurn;
    private ChessBoard board;

    private ChessPosition WKingPos;
    private ChessPosition BKingPos;




    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard(); //puts all the pieces down in their places
        this.thisTeamsTurn = TeamColor.WHITE; //White always starts in Chess
        WKingPos = new ChessPosition(1,5); //default Kings' positions in a game
        BKingPos = new ChessPosition(8,5);
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
    public Collection<ChessMove> validMoves(ChessPosition startPosition){
        ChessPiece currPiece = board.getPiece(startPosition);
        if (currPiece == null) {
            return null;
        }
        else{
            ChessGame.TeamColor currPieceTeam = this.board.getPiece(startPosition).getTeamColor();
            Collection <ChessMove> possibleMoves = currPiece.pieceMoves(board, startPosition);
            Collection <ChessMove> possibleMoves_TEMPLATE = currPiece.pieceMoves(board, startPosition);
            Collection <ChessMove> validMoves = new HashSet<>();

            try {
                if (isInCheck(currPieceTeam)) { //checking if in check on CURRENT board, not cloned board
                    for (ChessMove move : possibleMoves) {
                        ChessBoard clonedBoard = this.board.clone();
                        clonedBoard.addPiece(move.getEndPosition(), currPiece);
                        clonedBoard.removePiece(move.getStartPosition());
                        if (!isInCheck(clonedBoard, currPieceTeam)) { //using overloaded isInCheck to check potential consequences of a move
                            validMoves.add(move);
                            //if the piece could make a move that takes it OUT of check, that makes it valid
                        }
                    }
                }
                else{
                    for (ChessMove move : possibleMoves) {
                        ChessBoard clonedBoard = this.board.clone();
                        clonedBoard.addPiece(move.getEndPosition(), currPiece);
                        clonedBoard.removePiece(move.getStartPosition());
                        if (isInCheck(clonedBoard, currPieceTeam)){ //using overloaded isInCheck to check potential consequences of a move
                            possibleMoves_TEMPLATE.remove(move);
                        }
                    }
                    validMoves = possibleMoves_TEMPLATE;
                }
            }
            catch (CloneNotSupportedException e) {
                throw new RuntimeException("Bug in the code");
            }
            kingsFinder(this.board);//to set it back to original board
            return validMoves;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece pieceBeingMoved = this.board.getPiece(move.getStartPosition());
        Collection<ChessMove> currValidMoves = validMoves(move.getStartPosition()); //what if currValidMoves is empty?
        if(board.getPiece(move.getStartPosition()) == null){
            throw new InvalidMoveException();
        }
        else if (currValidMoves.isEmpty()){ //can't make a move that can't possibly exist
            throw new InvalidMoveException();
        }
        else if(!currValidMoves.contains(move)){ //any valid move should already be in Valid moves, and return false
            throw new InvalidMoveException();
        }
        else if(board.getPiece(move.getStartPosition()).getTeamColor() != thisTeamsTurn){ //not player's turn
            throw new InvalidMoveException();
        }
        else { //all test cases passed, a move now will be made
            if(pieceBeingMoved.getPieceType() == ChessPiece.PieceType.KING){
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
            else if (pieceBeingMoved.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null){
                ChessPiece newlyPromotedPawn = new ChessPiece(pieceBeingMoved.getTeamColor(), move.getPromotionPiece());
                this.board.addPiece(move.getEndPosition(), newlyPromotedPawn);
                this.board.removePiece(move.getStartPosition());
            }
            else{
                this.board.addPiece(move.getEndPosition(), pieceBeingMoved);
                this.board.removePiece(move.getStartPosition());
                //delete ChessPiece object at start pos of ChessMove in ChessBoard array, set to null.
                //Then set reference at end pos of ChessMove in ChessBoard array
            }
            //switches turns at the end of a move
            if (this.thisTeamsTurn == TeamColor.WHITE){
                this.thisTeamsTurn = TeamColor.BLACK;
            }
            else{
                this.thisTeamsTurn = TeamColor.WHITE;
            }
        }
    }


    private void kingsFinder(ChessBoard clonedBoard){
        //for autograder test scenarios
        boolean WKingFound = false;
        boolean BKingFound = false;

        for (int i = 1; i < 9; i++) { //Treat as row/column #s, not index values/one less
            for (int j = 1; j < 9; j++) {
                if (clonedBoard.getPiece(new ChessPosition(i, j)) != null && clonedBoard.getPiece(new ChessPosition(i, j)).getPieceType() == ChessPiece.PieceType.KING){
                    if (clonedBoard.getPiece(new ChessPosition(i, j)).getTeamColor() == TeamColor.WHITE){
                        this.WKingPos = new ChessPosition(i, j);
                        WKingFound = true;
                    }
                    else{
                        this.BKingPos = new ChessPosition(i, j);
                        BKingFound = true;
                    }
                }
            }
        }
        if (!WKingFound && !BKingFound){ //literally only for the test scenarios, this would never happen in a game
            WKingPos = null;
            BKingPos = null;
        }
    }

    private boolean wouldLandOnKing (Collection<ChessMove> movesProvided, TeamColor teamColor){
        for (ChessMove potentialMove : movesProvided) {
            if (teamColor == TeamColor.WHITE){
                if (potentialMove.getEndPosition().equals(WKingPos)) {
                    return true;
                }
            }
            else if (teamColor == TeamColor.BLACK){
                if (potentialMove.getEndPosition().equals(BKingPos)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //establishing Kings' locations
        kingsFinder(this.board);

        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (this.board.getPiece(new ChessPosition(i, j)) != null && this.board.getPiece(new ChessPosition(i, j)).getTeamColor() != teamColor) {
                    ChessPosition opponentPosition = new ChessPosition(i, j);
                    ChessPiece opponentPiece = this.board.getPiece(opponentPosition);
                    Collection<ChessMove> possibleCaptureMoves = opponentPiece.pieceMoves(this.board, opponentPosition);
                    if (wouldLandOnKing(possibleCaptureMoves, teamColor)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isInCheck(ChessBoard clonedBoard, TeamColor teamColor) { //OVERLOADED isInCheck FUNCTION
        kingsFinder(clonedBoard);

        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (clonedBoard.getPiece(new ChessPosition(i, j)) != null && clonedBoard.getPiece(new ChessPosition(i, j)).getTeamColor() != teamColor) {
                    ChessPosition opponentPosition = new ChessPosition(i, j);
                    ChessPiece opponentPiece = clonedBoard.getPiece(opponentPosition);
                    Collection<ChessMove> possibleCaptureMoves = opponentPiece.pieceMoves(clonedBoard, opponentPosition);
                    if (wouldLandOnKing(possibleCaptureMoves, teamColor)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean noValidMoves(ChessGame.TeamColor teamColor){
        Collection<ChessMove> anyValidMoves = new HashSet<>();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (this.board.getPiece(new ChessPosition(i, j)) != null && this.board.getPiece(new ChessPosition(i, j)).getTeamColor() == teamColor) {
                    ChessPosition teammatePosition = new ChessPosition(i, j);
                    Collection<ChessMove> movesPerPiece = validMoves(teammatePosition);
                    if (!movesPerPiece.isEmpty()){
                        anyValidMoves = validMoves(teammatePosition);
                    }
                }
            }
        }
        return anyValidMoves.isEmpty();
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)){
            return noValidMoves(teamColor);
        }
        else{
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor) && thisTeamsTurn == teamColor){
            return noValidMoves(teamColor);
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
        kingsFinder(this.board); //finds Kings' positions in a provided board/game (for the autograder tests
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
