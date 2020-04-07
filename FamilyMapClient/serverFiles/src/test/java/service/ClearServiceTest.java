package service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import DAO.UserDAO;
import model.Event;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import responses.ClearResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


/** Since there are no fail cases, we can find different scenario*/
public class ClearServiceTest {
    private Database db;

    private User userModel;
    private Event bestEvent;

    @BeforeEach
    public void setUp() throws Exception{
        db = new Database();
        userModel = new User("Jordan","123","123","123","123","124","125");
        bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
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
    public void clearPass1() throws Exception{

        UserDAO uDAO = new UserDAO(db.openConnection());
        uDAO.addUser(userModel);
        db.closeConnection(true);

        EventDAO eDAO = new EventDAO(db.openConnection());
        eDAO.insert(bestEvent);

        db.closeConnection(true);

        ClearService clearService = new ClearService();
        ClearResponse rp = clearService.clear();
        ClearResponse targetResponse = new ClearResponse();
        targetResponse.setMessage("Clear succeeded.");
        targetResponse.setSuccess(true);

        try {
            uDAO = new UserDAO(db.openConnection());
            User temp = uDAO.getUser(userModel.getUserName());
            assertNull(temp);
            db.closeConnection(true);
        }catch (DataAccessException error){
            db.closeConnection(false);
        }

        try {
            eDAO = new EventDAO(db.openConnection());
            Event event = eDAO.find(bestEvent.getEventID());
            assertNull(event);
            db.closeConnection(true);
        }catch (DataAccessException error){
            db.closeConnection(false);
        }

        assertEquals(rp,targetResponse);


    }

    @Test
    public void clearPass2() throws Exception{
        UserDAO uDAO = new UserDAO(db.openConnection());
        User userModel2 = new User("Jordan3","1234","123","123","123","124","1253");
        uDAO.addUser(userModel);
        uDAO.addUser(userModel2);
        db.closeConnection(true);

        ClearService clearService = new ClearService();
        ClearResponse rp = clearService.clear();
        ClearResponse targetResponse = new ClearResponse();
        targetResponse.setMessage("Clear succeeded.");
        targetResponse.setSuccess(true);
        try {
            uDAO = new UserDAO(db.openConnection());
            User temp = uDAO.getUser(userModel.getUserName());
            assertNull(temp);
            db.closeConnection(true);
        }catch (DataAccessException error){
            db.closeConnection(false);
        }

        assertEquals(rp,targetResponse);

    }

}
