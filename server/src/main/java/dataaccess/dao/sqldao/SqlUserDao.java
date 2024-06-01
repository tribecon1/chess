package dataaccess.dao.sqldao;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.dao.UserDao;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlUserDao implements UserDao {


    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        Connection conn = null;
        try (Connection autoCloseC = DatabaseManager.getConnection()){
            conn = autoCloseC;
            String sql = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, email);
                ps.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("Database Error: %s", e.getMessage()));
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        Connection conn;
        try (Connection autoCloseC = DatabaseManager.getConnection()){
            conn = autoCloseC;
            String query = "SELECT * FROM user WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                if (rs.next()){
                    return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
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
    public void clearUser() throws DataAccessException {
        DatabaseManager.clearTable("user");
    }

    @Override
    public int getDatabaseSize() throws DataAccessException {
        return DatabaseManager.getRowCount("user");
    }
}
