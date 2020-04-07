package DAO;


import model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

public class AuthTokenDAOTest {


    private Database db;
    private AuthToken bestAuth;

    @BeforeEach
    public void setUp() throws Exception {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        bestAuth = new AuthToken("123", "Jordan");
    }

    @AfterEach
    public void tearDown() throws Exception {
        //here we can get rid of anything from our tests we don't want to affect the rest of our program
        //lets clear the tables so that any data we entered for testing doesn't linger in our files
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    /***/
    @Test
    public void testGetAuthTokenPass() throws Exception {
        String compareTest = null;

        try {
            Connection conn = db.openConnection();
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);

            authTokenDAO.addAuthToken(bestAuth);
            compareTest = authTokenDAO.getUsername(bestAuth.getAuthToken());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertEquals(compareTest, bestAuth.getUserName());

    }

    /**Get an AuthToken Without passing into the database*/
    @Test
    public void testGetAuthTokenFail() throws Exception {

        String compareTest = null;
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);

            compareTest = authTokenDAO.getUsername(bestAuth.getAuthToken());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNull(compareTest);
    }

    /**
     * Pass in an authToken and get the username back
     */
    @Test
    public void testAddAuthTokenPass() throws Exception {
        String compareTest = null;

        try {
            Connection conn = db.openConnection();
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);

            authTokenDAO.addAuthToken(bestAuth);
            compareTest = authTokenDAO.getUsername(bestAuth.getAuthToken());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertEquals(compareTest, bestAuth.getUserName());
    }

    /**
     * Pass in a bad authToken and get a null username back
     */
    @Test
    public void testAddAuthTokenFail() throws Exception {

        String compareTest = null;
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);

            compareTest = authTokenDAO.getUsername(bestAuth.getAuthToken());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(compareTest);
    }

    /**Test the clear function*/
    @Test
    public void testClear() throws Exception {
        String compareTest = null;
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
            authTokenDAO.clear();
            compareTest = authTokenDAO.getUsername(bestAuth.getAuthToken());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNull(compareTest);

    }
}