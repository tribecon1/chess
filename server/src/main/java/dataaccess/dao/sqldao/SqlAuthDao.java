package dataaccess.dao.sqldao;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.dao.AuthDao;
import model.AuthData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlAuthDao implements AuthDao {


    @Override
    public AuthData createAuth(String username, String authToken) throws DataAccessException {
        Connection conn = null;
        try (Connection autoCloseC = DatabaseManager.getConnection()){
            conn = autoCloseC;
            String sql = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, authToken);
                ps.setString(2, username);
                ps.executeUpdate();
                return new AuthData(username, authToken);
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("Database Error: %s", e.getMessage()));
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        Connection conn;
        try (Connection autoCloseC = DatabaseManager.getConnection()){
            conn = autoCloseC;
            String query = "SELECT * FROM auth WHERE authToken = ?";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, authToken);
                ResultSet rs = ps.executeQuery();
                if (rs.next()){
                    return new AuthData(rs.getString("username"), rs.getString("authToken"));
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
    public void deleteAuth(String authToken) throws DataAccessException {
        Connection conn;
        try (Connection autoCloseC = DatabaseManager.getConnection()){
            conn = autoCloseC;
            String query = "DELETE FROM auth WHERE authToken = ?";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, authToken);
                ps.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("Database Error: %s", e.getMessage()));
        }
    }

    @Override
    public void clearAuth() throws DataAccessException {
        Connection conn = null;
        try (Connection autoCloseC = DatabaseManager.getConnection()){
            conn = autoCloseC;
            String sql = "DROP TABLE auth"; //if user replaced w/ ? and uncommented below, is code reusable for all 3 classes?
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("Database Error: %s", e.getMessage()));
        }
    }

    @Override
    public int getDatabaseSize() throws DataAccessException {
        int numberOfEntries = 0;
        Connection conn = null;
        try (Connection autoCloseC = DatabaseManager.getConnection()){
            conn = autoCloseC;
            String sql = "SELECT COUNT(*) FROM auth"; //if user replaced w/ ? and uncommented below, is code reusable for all 3 classes?
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                //ps.setString(1, "auth");
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    numberOfEntries = resultSet.getInt(1);
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("Database Error: %s", e.getMessage()));
        }
        return numberOfEntries;
    }

}
