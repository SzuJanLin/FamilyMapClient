package service;

import DAO.Database;
import DAO.EventDAO;
import DAO.PersonDAO;
import DAO.UserDAO;
import model.Event;
import model.Person;
import model.User;
import requests.LoadRequest;
import responses.LoadResponse;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * l
 * Load the user , persons and events.
 */
public class LoadService {
    public LoadService() {
    }

    /**
     * clear the data base and load data
     * data is consist of three arrays
     *
     * @param rq
     */
    public LoadResponse load(LoadRequest rq) {
        Database db = new Database();

        // get the arrays first
        ArrayList<User> users = rq.getUsers();
        ArrayList<Person> people = rq.getPersons();
        ArrayList<Event> events = rq.getEvents();

        LoadResponse rp = new LoadResponse();
        ClearService clearService = new ClearService();
        clearService.clear();
        try {

            Connection conn = db.openConnection();
            UserDAO userDAO = new UserDAO(conn);
            /**Load the Users here*/
            for (int i = 0; i < users.size(); i++) {
                User uTemp = users.get(i);

                if (uTemp.hasNull()) // assert there is no null
                    throw new Exception("Error: User was not complete.");

                userDAO.addUser(uTemp);
            }
            db.closeConnection(true);


            /**Load the persons here*/
            conn = db.openConnection();
            PersonDAO personDAO = new PersonDAO(conn);
            for (int i = 0; i < people.size(); i++) {
                Person pTemp = people.get(i);
                if (pTemp.hasNull())// assert there is no null
                    throw new Exception("Error: Person was not complete.");

                personDAO.addPerson(pTemp);
            }
            db.closeConnection(true);

            /**Load the events here*/
            conn = db.openConnection();
            EventDAO eventDAO = new EventDAO(conn);
            for (int i = 0; i < events.size(); i++) {
                Event eTemp = events.get(i);
                if (eTemp.hasNull())// assert there is no null
                    throw new Exception("Error: Event was not complete.");
                eventDAO.insert(eTemp);
            }
            String message = "Successfully added " + users.size() + " users, "
                    + people.size() + " persons, and " + events.size() +
                    " events to the database.";
            rp.setMessage(message);
            rp.setSuccess(true);
            db.closeConnection(true);

        } catch (Exception e) {
            try {
                db.closeConnection(false);
            } catch (Exception closeError) {
                closeError.printStackTrace();
            }

            rp.setMessage(e.getMessage());
            rp.setSuccess(false);
            e.printStackTrace();
        }
        return rp;
    }

}
