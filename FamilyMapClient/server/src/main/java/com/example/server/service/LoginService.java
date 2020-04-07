package com.example.server.service;


import com.example.server.DAO.AuthTokenDAO;
import com.example.server.DAO.DataAccessException;
import com.example.server.DAO.Database;
import com.example.server.DAO.UserDAO;
import com.example.shared.model.AuthToken;
import com.example.shared.model.User;
import com.example.shared.requests.LoginRequest;
import com.example.shared.responses.LoginResponse;

import java.sql.Connection;
import java.util.UUID;

/**
 * login service
 */
public class LoginService {
    public LoginService() {
    }

    /**
     * login for the user
     *
     * @param rq
     * @return
     */
    public LoginResponse login(LoginRequest rq) {
        String username = rq.getUserName();
        String password = rq.getPassword();
        String authToken = null;
        String personID = null;
        LoginResponse response = new LoginResponse();

        Database db = new Database();
        try {
            User temp = null;
            try {
                //create authToken
                Connection conn = db.openConnection();
                AuthTokenDAO authDao = new AuthTokenDAO(conn);
                authToken = UUID.randomUUID().toString();
                AuthToken tempAuth = new AuthToken(authToken, username);
                authDao.addAuthToken(tempAuth);
                db.closeConnection(true);

                conn = db.openConnection();
                UserDAO uDao = new UserDAO(conn);
                temp = uDao.getUser(username);
                db.closeConnection(true);
            } catch (DataAccessException error) {
                error.printStackTrace();
            }


            //check the password is valid
            if (temp != null) {
                if (password.equals(temp.getPassword()))
                    personID = temp.getPersonID();
                else {
                    throw new Exception("ERROR: wrong password");
                }
            } else {
                throw new Exception("Error: User not found");
            }

            //context cannot be null
            if (authToken == null || username == null || personID == null) {
                throw new Exception("ERROR: context cannot be null");
            }


            response = new LoginResponse(authToken, username, personID, true);

        } catch (Exception e) {
            response = new LoginResponse(e.getMessage(), false);
            e.printStackTrace();
        }

        return response;
    }
}
