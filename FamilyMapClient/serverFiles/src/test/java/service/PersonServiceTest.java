package service;

import DAO.*;
import model.AuthToken;
import model.Event;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.EventRequest;
import requests.PersonRequest;
import responses.EventResponse;
import responses.EventsResponse;
import responses.PersonResponse;
import responses.PersonsResponse;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonServiceTest {
    private AuthToken AuthTest;
    private Person firstPerson, secondPerson;
    private Database db;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        AuthTest = new AuthToken("12345", "jordan");
        firstPerson = new Person("123", "jordan", "is",
                "cool", "m", "29.3", "yes", "River");
        secondPerson = new Person("125", "jordan", "is",
                "cool", "f", "239.3", "ye3s", "Ri4ver");
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void getPersonTestPass() throws Exception {
        /**Set up the database*/
        try {
            AuthTokenDAO authDao = new AuthTokenDAO(db.openConnection());
            authDao.addAuthToken(AuthTest);
            db.closeConnection(true);
        } catch (DataAccessException error) {
            db.closeConnection(false);
        }
        try {
            PersonDAO pDao = new PersonDAO(db.openConnection());
            pDao.addPerson(firstPerson);
            db.closeConnection(true);
        } catch (DataAccessException error) {
            db.closeConnection(false);
        }

        /**Now we can try to call the Event Service*/
        PersonService pService = new PersonService();

        PersonRequest testRequest = new PersonRequest("12345", "123");
        PersonResponse testResponse = new PersonResponse("123", "jordan", "is",
                "cool", "m", "29.3", "yes", "River", null, true);

        PersonResponse output = pService.getPerson(testRequest);

        assertEquals(output, testResponse);
    }

    @Test
    public void getPersonTestFail() throws Exception {
        /**Set up the database*/
        try {
            AuthTokenDAO authDao = new AuthTokenDAO(db.openConnection());
            authDao.addAuthToken(AuthTest);
            db.closeConnection(true);
        } catch (DataAccessException error) {
            db.closeConnection(false);
        }
        try {
            PersonDAO pDao = new PersonDAO(db.openConnection());
            pDao.addPerson(firstPerson);
            db.closeConnection(true);
        } catch (DataAccessException error) {
            db.closeConnection(false);
        }

        /**Now we can try to call the Event Service*/
        PersonService pService = new PersonService();

        /** Try to output a bad respond with wrong authToken request*/
        PersonRequest testRequest = new PersonRequest("1234", "event1");
        /**Return a response that has nothing inside*/
        PersonResponse testResponse = new PersonResponse();
        testResponse.setMessage("ERROR: Person not found.");
        PersonResponse output = pService.getPerson(testRequest);

        assertEquals(output, testResponse);
    }

    @Test
    public void getPeoplePass() throws Exception {
        /**Set up the database*/
        try {
            AuthTokenDAO authDao = new AuthTokenDAO(db.openConnection());
            authDao.addAuthToken(AuthTest);
            db.closeConnection(true);
        } catch (DataAccessException error) {
            db.closeConnection(false);
        }
        try {
            PersonDAO pDao = new PersonDAO(db.openConnection());
            pDao.addPerson(firstPerson);
            pDao.addPerson(secondPerson);
            db.closeConnection(true);
        } catch (DataAccessException error) {
            db.closeConnection(false);
        }

        /**Now we can try to call the Person Service*/
        PersonService pService = new PersonService();

        PersonRequest testRequest = new PersonRequest("12345", "123");
        Person test1 = new Person("123", "jordan", "is",
                "cool", "m", "29.3", "yes", "River");
        Person test2 = new Person("125", "jordan", "is",
                "cool", "f", "239.3", "ye3s", "Ri4ver");
        ArrayList<Person> ArrayPerson = new ArrayList<>();
        ArrayPerson.add(test1);
        ArrayPerson.add(test2);

        PersonsResponse expectedOutput = new PersonsResponse(ArrayPerson, true);

        PersonsResponse output = pService.getPeople(testRequest);
        assertEquals(output, expectedOutput);
    }

    @Test
    public void getPeopleFail() throws Exception {
        /**Set up the database*/
        try {
            AuthTokenDAO authDao = new AuthTokenDAO(db.openConnection());
            authDao.addAuthToken(AuthTest);
            db.closeConnection(true);
        } catch (DataAccessException error) {
            db.closeConnection(false);
        }
        try {
            PersonDAO pDao = new PersonDAO(db.openConnection());
            pDao.addPerson(firstPerson);
            pDao.addPerson(secondPerson);
            db.closeConnection(true);
        } catch (DataAccessException error) {
            db.closeConnection(false);
        }

        /**Now we can try to call the Person Service*/
        PersonService pService = new PersonService();

        PersonRequest testRequest = new PersonRequest("1234", "123");

        PersonsResponse expectedOutput = new PersonsResponse(false, "ERROR: No User Associated.");


        PersonsResponse output = pService.getPeople(testRequest);
        assertEquals(output,expectedOutput);
    }


}
