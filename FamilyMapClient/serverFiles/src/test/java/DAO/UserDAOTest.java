package DAO;


import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {


    private Database db;
    private UserDAO uDAO;
    private User testModel;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        testModel = new User("Jordan","123","123","123","123","124","125");
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
    public void getUserPass() throws Exception
    {
        User output = null;
        try {
            Connection conn = db.openConnection();
            uDAO = new UserDAO(conn);
            uDAO.addUser(testModel);
            output = uDAO.getUser(testModel.getUserName());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            //If we catch an exception we will end up in here, where we can change our boolean to
            //false to show that our function failed to perform correctly
            db.closeConnection(false);
        }
        //Check to make sure the return is null
        assertEquals(output,testModel);
    }
    @Test
    public void getUserFail() throws Exception
    {
        User output = null;
        try {
            Connection conn = db.openConnection();
            uDAO = new UserDAO(conn);
            output = uDAO.getUser(testModel.getUserName());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            //If we catch an exception we will end up in here, where we can change our boolean to
            //false to show that our function failed to perform correctly
            db.closeConnection(false);
        }
        //Check to make sure the return is null
        assertNull(output);
    }

    @Test
    public void insertFail() throws Exception
    {
        boolean didItWork = true;
        try {
            Connection conn = db.openConnection();
            uDAO = new UserDAO(conn);
            uDAO.addUser(testModel);
            uDAO.addUser(testModel);


            db.closeConnection(true);
        } catch (DataAccessException e) {
            //If we catch an exception we will end up in here, where we can change our boolean to
            //false to show that our function failed to perform correctly
            db.closeConnection(false);
            didItWork = false;
        }
        //Check to make sure the return is null

        assertFalse(didItWork);

       User compareTest = testModel;
        try {
            Connection conn = db.openConnection();
            UserDAO uDAO = new UserDAO(conn);
            //and then get something back from our find. If the event is not in the database we
            //should have just changed our compareTest to a null object
            compareTest = uDAO.getUser(testModel.getUserName());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        //Now make sure that compareTest is indeed null
        assertNull(compareTest);
    }
    @Test
    public void insertPass() throws Exception
    {
        User compareTest = null;
        try {
            Connection conn = db.openConnection();
            uDAO = new UserDAO(conn);
            uDAO.addUser(testModel);

            compareTest = uDAO.getUser(testModel.getUserName());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            //If we catch an exception we will end up in here, where we can change our boolean to
            //false to show that our function failed to perform correctly
            db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(testModel,compareTest);
    }

    @Test
    public void deletePass() throws Exception
    {
        User output = null;
        try {
            Connection conn = db.openConnection();
            uDAO = new UserDAO(conn);
            uDAO.addUser(testModel);
            uDAO.delete(testModel.getUserName());
            output = uDAO.getUser(testModel.getUserName());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            //If we catch an exception we will end up in here, where we can change our boolean to
            //false to show that our function failed to perform correctly
            db.closeConnection(false);
        }
        //Check to make sure the return is null
        assertNull(output);
    }

    @Test
    public void clear() throws Exception{
        User output = null;
        try{
            Connection conn = db.openConnection();
            uDAO = new UserDAO(conn);
            uDAO.clear();
            output = uDAO.getUser(testModel.getUserName());
            db.closeConnection(true);
        }catch (DataAccessException e)
        {
            db.closeConnection(false);
        }


        assertNull(output);

    }

}