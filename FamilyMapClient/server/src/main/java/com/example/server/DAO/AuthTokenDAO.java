package com.example.server.DAO;

import com.example.shared.model.AuthToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * the class to handle authToken and the usernames
 */
public class AuthTokenDAO {
    private final Connection conn;

    /**
     * initialize the database
     *
     * @param conn
     * @throws Exception
     */
    public AuthTokenDAO(Connection conn) throws Exception {
        this.conn = conn;
        //Every time when we calls the class we make sure the table is there, if not create one

    }

    /**
     * find  a current token
     *
     * @param authToken
     * @return
     */
    public String getUsername(String authToken) {
        String sql = "SELECT username" +
                " FROM AuthToken WHERE authToken = ?";

        String output = null;
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, authToken);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                output = rs.getString(1);

            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;


    }


    /**
     * create  an AuthToken for the user
     *
     * @param username
     */
    public void addAuthToken(AuthToken username) {
        String sql = "INSERT INTO AuthToken(authToken,username) VALUES(?,?)";
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username.getAuthToken());
            pstmt.setString(2, username.getUserName());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                pstmt.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Clear the AuthToken Table
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM AuthToken";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Fail to delete");
        }
    }

}