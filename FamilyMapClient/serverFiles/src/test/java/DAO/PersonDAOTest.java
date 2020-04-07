package DAO;


import model.AuthToken;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDAOTest {


    private Database db;
    private Person testModel;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        testModel = new Person("123", "jordan", "is",
                "cool", "m", "29.3", "yes", "River");

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
    public void insertPass() throws Exception {
        Person compareTest = null;
        try {
            //Let's get our connection and make a new DAO
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            //While insert returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            pDao.addPerson(testModel);
            //So lets use a find method to get the event that we just put in back out
            compareTest = pDao.getPerson(testModel.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(testModel, compareTest);
    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            //While insert returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            pDao.addPerson(testModel);
            pDao.addPerson(testModel);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            //If we catch an exception we will end up in here, where we can change our boolean to
            //false to show that our function failed to perform correctly
            db.closeConnection(false);
            didItWork = false;
        }
        //Check to make sure that we did in fact enter our catch statement
        assertFalse(didItWork);

        //Since we know our database encountered an error, both instances of insert should have been
        //rolled back. So for added security lets make one more quick check using our find function
        //to make sure that our event is not in the database
        //Set our compareTest to an actual event
        Person compareTest = testModel;
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            //and then get something back from our find. If the event is not in the database we
            //should have just changed our compareTest to a null object
            compareTest = pDao.getPerson(testModel.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        //Now make sure that compareTest is indeed null
        assertNull(compareTest);
    }

    @Test
    public void findFail() throws Exception {
        boolean didItWork = true;
        Person output = null;
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            output = pDao.getPerson(testModel.getPersonID());

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
    public void findPass() throws Exception {
        Person output = null;
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            pDao.addPerson(testModel);
            output = pDao.getPerson(testModel.getPersonID());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            //If we catch an exception we will end up in here, where we can change our boolean to
            //false to show that our function failed to perform correctly
            db.closeConnection(false);
        }
        //Check to make sure the return is null
        assertEquals(output, testModel);
    }

    @Test
    public void findPeoplePass() throws Exception {
        ArrayList<Person> output = null;
        Person testModel2 = null;
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            pDao.addPerson(testModel);
            testModel2 = new Person("124", "jordan", "yes",
                    "dan", "f", "29.3", "aji", "erd");
            pDao.addPerson(testModel2);
            output = pDao.getPeople(testModel.getAssociatedUsername());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            //If we catch an exception we will end up in here, where we can change our boolean to
            //false to show that our function failed to perform correctly
            db.closeConnection(false);
        }
        //Check to make sure the return is null
        assertEquals(output.get(0), testModel);
        assertEquals(output.get(1), testModel2);

    }
    @Test
    public void findPeopleFail() throws Exception{
        ArrayList<Person> output = null;
        Person testModel2 = null;
        try{
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);

            output = pDao.getPeople(testModel.getAssociatedUsername());
            db.closeConnection(true);
        }catch (DataAccessException e){
            db.closeConnection(false);
        }

        assertNull(output);

    }



    @Test
    public void clear() throws Exception {
        Person output = null;
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            pDao.clear();
            output = pDao.getPerson(testModel.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(output);
    }


}