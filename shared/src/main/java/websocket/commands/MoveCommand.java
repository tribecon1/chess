package websocket.commands;

import chess.ChessMove;

public class MoveCommand extends UserGameCommand{
    private final ChessMove move;

    public MoveCommand(String authToken, int gameID, ChessMove givenMove) {
        super(authToken, gameID);
        this.commandType = CommandType.MAKE_MOVE;
        this.move = givenMove;
    }

    public ChessMove getMove() {
        return this.move;
    }
}
