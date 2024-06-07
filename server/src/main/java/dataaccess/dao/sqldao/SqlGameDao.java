package dataaccess.dao.sqldao;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.dao.GameDao;
import model.GameData;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import server.SerializerDeserializer;

public class SqlGameDao implements GameDao {


    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        Connection conn;
        ChessGame newGame = new ChessGame();
        try (Connection autoCloseC = DatabaseManager.getConnection()){
            conn = autoCloseC;
            String sql = "INSERT INTO game (gameName, game) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                ps.setString(1, gameName);
                ps.setString(2, SerializerDeserializer.convertToJSON(newGame));
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()){
                    return new GameData(rs.getInt(1), null, null, gameName, newGame);
                }
                else{
                    return null;
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("Database Error: %s", e.getMessage()));
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        Connection conn;
        try (Connection autoCloseC = DatabaseManager.getConnection()){
            conn = autoCloseC;
            String query = "SELECT * FROM game WHERE gameID = ?";
            try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, gameID);
                ResultSet rs = ps.executeQuery();
                if (rs.next()){
                    ChessGame currChessGame = SerializerDeserializer.convertFromJSON(rs.getString("game"), ChessGame.class);
                    return new GameData(rs.getInt("gameID"), rs.getString("whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"), currChessGame);
                }
                else{
                    return null;
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("Database Error: %s", e.getMessage()));
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> gamesList = new ArrayList<>();
        Connection conn;
        try (Connection autoCloseC = DatabaseManager.getConnection()){
            conn = autoCloseC;
            String query = "SELECT gameID, whiteUsername, blackUsername, gameName FROM game";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    int gameID = rs.getInt("gameID");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    gamesList.add(new GameData(gameID, whiteUsername, blackUsername, gameName, null));
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("Database Error: %s", e.getMessage()));
        }
        return gamesList;
    }

    @Override
    public void updateGame(GameData currGame, GameData updatedGame) throws DataAccessException {
        Connection conn;
        try (Connection autoCloseC = DatabaseManager.getConnection()){
            conn = autoCloseC;
            String sql = "UPDATE game " + "SET whiteUsername = ?, blackUsername = ?, game = ? " + "WHERE gameID = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, updatedGame.whiteUsername());
                ps.setString(2, updatedGame.blackUsername());
                ps.setString(3, SerializerDeserializer.convertToJSON(updatedGame));
                ps.setInt(4, currGame.gameID());
                ps.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("Database Error: %s", e.getMessage()));
        }
    }

    @Override
    public void clearGame() throws DataAccessException {
        DatabaseManager.clearTable("game");
    }

    @Override
    public boolean getGameByName(String gameName) throws DataAccessException {
        Connection conn;
        try (Connection autoCloseC = DatabaseManager.getConnection()){
            conn = autoCloseC;
            String query = "SELECT * FROM game WHERE gameName = ?";
            try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, gameName);
                ResultSet rs = ps.executeQuery();
                return rs.next();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("Database Error: %s", e.getMessage()));
        }
    }

    @Override
    public int getDatabaseSize() throws DataAccessException {
        return DatabaseManager.getRowCount("game");
    }
}
