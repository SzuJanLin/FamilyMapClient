package service;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import model.AuthToken;
import model.Event;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.EventRequest;
import responses.EventResponse;
import responses.EventsResponse;

import static org.junit.jupiter.api.Assertions.*;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class EventServiceTest {

    private AuthToken AuthTest;
    private Event firstEvent, secondEvent;
    private Database db;
    /**
     * Set up the Environment for the Test, using a database to make sure all the data we want is in this test case*/
    @BeforeEach
    public void setUp() throws Exception{
        db = new Database();
        AuthTest= new AuthToken("12345","jordan");
        firstEvent = new Event("event1", "jordan", "jordan1",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        secondEvent = new Event("event2", "jordan", "jordan1",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);

    }

    @AfterEach
    public void tearDown() throws Exception{
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void getEventTestPass() throws Exception{
        /**Set up the database*/
        try {
            AuthTokenDAO authDao = new AuthTokenDAO(db.openConnection());
            authDao.addAuthToken(AuthTest);
            db.closeConnection(true);
        }catch (DataAccessException error){
            db.closeConnection(false);
        }
        try {
            EventDAO eDao = new EventDAO(db.openConnection());
            eDao.insert(firstEvent);
            db.closeConnection(true);
        }catch (DataAccessException error){
            db.closeConnection(false);
        }

        /**Now we can try to call the Event Service*/
        EventService eService = new EventService();

        EventRequest testRequest = new EventRequest("12345","event1");
        EventResponse testResponse = new EventResponse("event1","jordan", "jordan1",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016,null,true);

       EventResponse output =  eService.getEvent(testRequest);

       assertEquals(output,testResponse);
    }

    @Test
    public void getEventTestFail() throws Exception{
        try {
            AuthTokenDAO authDao = new AuthTokenDAO(db.openConnection());
            authDao.addAuthToken(AuthTest);
            db.closeConnection(true);
        }catch (DataAccessException error){
            db.closeConnection(false);
        }
        try {
            EventDAO eDao = new EventDAO(db.openConnection());
            eDao.insert(firstEvent);
            db.closeConnection(true);
        }catch (DataAccessException error){
            db.closeConnection(false);
        }

        /**Now we can try to call the Event Service*/
        EventService eService = new EventService();

        /** Try to output a bad respond with wrong authToken request*/
        EventRequest testRequest = new EventRequest("1234","event1");
        EventResponse testResponse = new EventResponse();
        testResponse.setMessage("ERROR: Not your ancestors event");

        EventResponse output =  eService.getEvent(testRequest);

        assertEquals(output.getMessage(),testResponse.getMessage());
        assertFalse(output.isSuccess());

    }



    @Test
    public void getEventsTestPass() throws Exception{
            /**Set up the database*/
            try {
                AuthTokenDAO authDao = new AuthTokenDAO(db.openConnection());
                authDao.addAuthToken(AuthTest);
                db.closeConnection(true);
            }catch (DataAccessException error){
                db.closeConnection(false);
            }
            try {
                EventDAO eDao = new EventDAO(db.openConnection());
                eDao.insert(firstEvent);
                eDao.insert(secondEvent);
                db.closeConnection(true);
            }catch (DataAccessException error){
                db.closeConnection(false);
            }

            /**Now we can try to call the Event Service*/
            EventService eService = new EventService();

            EventRequest testRequest = new EventRequest("12345","event1");
            Event test1 = new Event("event1","jordan", "jordan1",
                    10.3f, 10.3f, "Japan", "Ushiku",
                    "Biking_Around", 2016);
            Event test2 = new Event("event2", "jordan", "jordan1",
                    10.3f, 10.3f, "Japan", "Ushiku",
                    "Biking_Around", 2016);
            ArrayList<Event> ArrayTest = new ArrayList<>();
            ArrayTest.add(test1);
            ArrayTest.add(test2);

            EventsResponse expectedOutput = new EventsResponse(ArrayTest, true);



            EventsResponse output =  eService.getEvents(testRequest);

            assertEquals(output,expectedOutput);
    }

    @Test
    public void getEventsTestFail() throws Exception{
        try {
            AuthTokenDAO authDao = new AuthTokenDAO(db.openConnection());
            authDao.addAuthToken(AuthTest);
            db.closeConnection(true);
        }catch (DataAccessException error){
            db.closeConnection(false);
        }
        try {
            EventDAO eDao = new EventDAO(db.openConnection());
            eDao.insert(firstEvent);
            eDao.insert(secondEvent);
            db.closeConnection(true);
        }catch (DataAccessException error){
            db.closeConnection(false);
        }

        /**Now we can try to call the Event Service*/
        EventService eService = new EventService();

        EventRequest testRequest = new EventRequest("1234","event2");

        EventsResponse output =  eService.getEvents(testRequest);
        EventsResponse expectedOutput = new EventsResponse(false,"ERROR: No User Associated");
        assertEquals(output,expectedOutput);

    }


}
