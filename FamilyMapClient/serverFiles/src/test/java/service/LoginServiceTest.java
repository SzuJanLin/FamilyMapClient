package service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.LoginRequest;
import responses.LoadResponse;
import responses.LoginResponse;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {

    private Database db;
    private Person testModel;

    private User user;

    @BeforeEach
    public void setUp() throws Exception{
        db = new Database();
        user = new User("jordan", "szujanlin", "123",
                "su", "lin", "m", "12345");

    }

    @AfterEach
    public void tearDown() throws Exception {
        //here we can get rid of anything from our tests we don't want to affect the rest of our program
        //lets clear the tables so that any data we entered for testing doesn't linger in our files

        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void loginPass() throws Exception{
        /** add user first*/
        try {
            UserDAO userDAO = new UserDAO(db.openConnection());
            userDAO.addUser(user);
            db.closeConnection(true);
        }catch (DataAccessException error){
            db.closeConnection(false);
            error.printStackTrace();
        }

        LoginRequest request = new LoginRequest("jordan","szujanlin");


        LoginService loginService = new LoginService();
        LoginResponse output =  loginService.login(request);

        LoginResponse loginResponse = new LoginResponse("we can't check", "jordan","12345",true);

        /**Since the Login function will randomly generate authToken ID, so we are going to check everything other than auth*/
        assertEquals(output,loginResponse);
    }

    @Test
    public void loginFailPassword() throws Exception{
        /** add user first*/
        try {
            UserDAO userDAO = new UserDAO(db.openConnection());
            userDAO.addUser(user);
            db.closeConnection(true);
        }catch (DataAccessException error){
            db.closeConnection(false);
            error.printStackTrace();
        }

        LoginRequest request = new LoginRequest("jordan","szujanlin3");


        LoginService loginService = new LoginService();
        LoginResponse output =  loginService.login(request);

        LoginResponse loginResponse = new LoginResponse( "ERROR: wrong password",false);
        assertEquals(output,loginResponse);
    }

    @Test
    public void loginFailUserNotFound() throws Exception{
        /** add user first*/
        try {
            UserDAO userDAO = new UserDAO(db.openConnection());
            userDAO.addUser(user);
            db.closeConnection(true);
        }catch (DataAccessException error){
            db.closeConnection(false);
            error.printStackTrace();
        }

        LoginRequest request = new LoginRequest("jordanLin","szujanlin3");


        LoginService loginService = new LoginService();
        LoginResponse output =  loginService.login(request);

        LoginResponse loginResponse = new LoginResponse("Error: User not found",false);
        assertEquals(output,loginResponse);
    }



}
