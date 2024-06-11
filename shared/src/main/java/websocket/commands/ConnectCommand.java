package websocket.commands;

public class ConnectCommand extends UserGameCommand{

    private final int gameID;

    public ConnectCommand(String authToken, int gameID) {
        super(authToken);
        this.commandType = CommandType.CONNECT;
        this.gameID = gameID;
    }

    public int getGameID() {
        return this.gameID;
    }

}
