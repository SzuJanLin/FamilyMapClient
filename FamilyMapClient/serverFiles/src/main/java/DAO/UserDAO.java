package DAO;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * the class to handle users
 */
public class UserDAO {


    private final Connection conn;

    /**
     * initialize the database
     *
     * @param conn
     * @throws Exception
     */
    public UserDAO(Connection conn) throws Exception {
        this.conn = conn;
    }


    /**
     * pass in a LoginRequest and get the user's information
     *
     * @param username
     * @return
     */
    public User getUser(String username) throws DataAccessException {
        User user;
        ResultSet rs = null;
        String sql = "SELECT username, password, email" +
                ",first_name, last_name, gender, personId" +
                " FROM User WHERE username = ?";
        User output = null;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                output = new User(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getString(4), rs.getString(5)
                        , rs.getString(6), rs.getString(7));
                return output;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while finding user");
        }

        return null;
    }


    /**
     * addUser user to the database
     *
     * @param user
     */
    public void addUser(User user) throws DataAccessException {
        String sql = "INSERT INTO User(username,password,email" +
                ",first_name, last_name, gender, personId) VALUES(?,?,?,?,?,?,?)";
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getFirstName());
            pstmt.setString(5, user.getLastName());
            pstmt.setString(6, user.getGender());
            pstmt.setString(7, user.getPersonID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }


    }

    /**
     * delete a username from database for testing purpose
     *
     * @param username
     */
    public void delete(String username) {

        String sql = "DELETE FROM User Where username = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void clear() throws DataAccessException {
        String sql = "DELETE FROM User";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Fail to delete");
        }
    }
}
