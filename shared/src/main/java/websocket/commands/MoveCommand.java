package websocket.commands;

import chess.ChessMove;

public class MoveCommand extends UserGameCommand{
    private final int gameID;
    private final ChessMove move;


    public MoveCommand(String authToken, int gameID, ChessMove givenMove) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.gameID = gameID;
        this.move = givenMove;
    }

    public int getGameID() {
        return this.gameID;
    }
    public ChessMove getMove() {
        return this.move;
    }
}
