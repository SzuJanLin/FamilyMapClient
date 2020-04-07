package service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import jsonTempClass.Location;
import jsonTempClass.Locations;
import jsonTempClass.Names;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import responses.FillResponse;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FillServiceTest {

    private Database db;
    private User user;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        user = new User("jordan", "szujanlin", "123",
                "su", "lin", "m", "12345");

    }

    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }


    @Test
    public void fillTestPass() throws Exception {
        FillService fillService = new FillService();
        /**First we need to set up the user in the database*/
        try {
            UserDAO userDAO = new UserDAO(db.openConnection());
            userDAO.addUser(user);
            db.closeConnection(true);
        } catch (DataAccessException error) {
            db.closeConnection(false);
            error.printStackTrace();
        }

        /**Set up some locations*/
        Location location1 = new Location("Taiwan", "Taichung", "1.234", "435");
        Location location2 = new Location("US", "New York", "145", "12.345");
        ArrayList<Location> arrayLocations = new ArrayList<>();
        arrayLocations.add(location1);
        arrayLocations.add(location2);
        Locations locations = new Locations(arrayLocations);

        /**Set up some Names*/
        ArrayList<String> names = new ArrayList<>();
        names.add("Peter");
        names.add("James");
        names.add("Chris");
        Names randomNames = new Names(names);


        FillResponse output = fillService.fill("jordan", 2, locations, randomNames, randomNames, randomNames);
        FillResponse fillResponse = new FillResponse("Successfully added 7 persons and 19 events to the database.",true);
        assertEquals(output, fillResponse);
    }

    /**
     * Try to fill without a username
     */
    @Test
    public void fillTestFail() throws Exception {
        FillService fillService = new FillService();

        /**Set up some locations*/
        Location location1 = new Location("Taiwan", "Taichung", "1.234", "435");
        Location location2 = new Location("US", "New York", "145", "12.345");
        ArrayList<Location> arrayLocations = new ArrayList<>();
        arrayLocations.add(location1);
        arrayLocations.add(location2);
        Locations locations = new Locations(arrayLocations);

        /**Set up some Names*/
        ArrayList<String> names = new ArrayList<>();
        names.add("Peter");
        names.add("James");
        names.add("Chris");
        Names randomNames = new Names(names);

        /** Pass in an invalid User Name*/
        FillResponse output = fillService.fill("jordans", 3, locations, randomNames, randomNames, randomNames);
        FillResponse fillResponse = new FillResponse("Error: User does not exist.",false);
        assertEquals(output, fillResponse);
    }

}
