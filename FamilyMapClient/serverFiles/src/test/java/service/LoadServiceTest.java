package service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.LoadRequest;
import responses.LoadResponse;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class LoadServiceTest {
    private Database db;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

   @Test
    public void loadPass() throws Exception{
        /**Set up the request */
        ArrayList<User> users = new ArrayList<>();
        ArrayList<Person> people = new ArrayList<>();
        ArrayList<Event> events = new ArrayList<>();

        users.add(new User("Jordan","123",
                "123","123","123","m","125"));
        users.add(new User("JordanLin","12345",
                "123","iam","jordan","f","125"));
        people.add(new Person("di123", "jordan", "is",
                "cool", "m", "29.3", "yes", "River"));
       people.add(new Person("12334", "jordan2", "is",
               "cool", "m", "29.3", "yes", "River"));
       events.add(new Event("Biking_123A", "Gale", "Gale123A",
               10.3f, 10.3f, "Japan", "Ushiku",
               "Biking_Around", 2016));
       events.add(new Event("Biking_123B", "Gale", "Gale123B",
               10.3f, 10.3f, "Japan", "Ushike",
               "Biking_Around", 2015));

       LoadRequest loadRequest = new LoadRequest(users,people,events);

       /**And now we can call the service*/
       LoadService loadService = new LoadService();
       LoadResponse output =loadService.load(loadRequest);
       LoadResponse loadResponse = new LoadResponse("Successfully added 2 users, 2 persons, and 2 events to the database.");
       assertEquals(output,loadResponse);

   }

   /**Add with some missing parts*/
   @Test
    public void loadFail() throws Exception{
       /**Set up the request */
       ArrayList<User> users = new ArrayList<>();
       ArrayList<Person> people = new ArrayList<>();
       ArrayList<Event> events = new ArrayList<>();

       users.add(new User("Jordan","123",
               "123","123","123","m","125"));
       users.add(new User("JordanLin",null,
               "123","iam","jordan","f","125"));
       people.add(new Person(null, "jordan", "is",
               "cool", "m", "29.3", "yes", "River"));
       people.add(new Person("12334", "jordan2", "is",
               "cool", "m", "29.3", "yes", "River"));
       events.add(new Event("Biking_123A", "Gale", "Gale123A",
               10.3f, 10.3f, "Japan", "Ushiku",
               "Biking_Around", 2016));
       events.add(new Event(null, "Gale", "Gale123B",
               10.3f, 10.3f, "Japan", "Ushike",
               "Biking_Around", 2015));

       LoadRequest loadRequest = new LoadRequest(users,people,events);

       /**And now we can call the service*/
       LoadService loadService = new LoadService();
       LoadResponse output =loadService.load(loadRequest);
       LoadResponse loadResponse = new LoadResponse("Error: User was not complete.");
       assertEquals(output,loadResponse);
   }


}

