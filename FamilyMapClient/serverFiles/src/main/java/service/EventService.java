package service;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import model.Event;
import requests.EventRequest;
import responses.EventResponse;
import responses.EventsResponse;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * event service
 */
public class EventService {
    public EventService() {
    }

    /**
     * get a event
     *
     * @param rq
     * @return
     */
    public EventResponse getEvent(EventRequest rq) {
        // get the info
        String authToken = rq.getAuthToken();
        String eventId = rq.getEvent_id();

        // response
        EventResponse response = new EventResponse();
        String username = null;

        Database db = new Database();
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO authDao = new AuthTokenDAO(conn);
            EventDAO eDao = new EventDAO(conn);
            username = authDao.getUsername(authToken);

            Event eModel = eDao.find(eventId);

            /**Error checking here*/
            if (eModel == null) {
                throw new Exception("ERROR: Event not Found");
            }

            if (!eModel.getAssociatedUsername().equals(username))
            // make sure the event belongs to the user
            {

                throw new Exception("ERROR: Not your ancestors event");
            }


            response = new EventResponse(eModel.getEventID(),
                    eModel.getAssociatedUsername(), eModel.getPersonID(),
                    eModel.getLatitude(), eModel.getLongitude(),
                    eModel.getCountry(), eModel.getCity(),
                    eModel.getEventType(), eModel.getYear(),
                    null, true);
            db.closeConnection(true);

        } catch (Exception e) {
            try {
                db.closeConnection(false);
            } catch (Exception closeConnectionError) {
                System.out.println("ERROR: Close connection error");
            }

            response = new EventResponse(null, null, null, null,
                    null, null, null, null, null, e.getMessage(), false);
        }

        return response;
    }


    /**
     * return an array of the Event response objects
     *
     * @param rq
     * @return
     */
    public EventsResponse getEvents(EventRequest rq) {

        String authToken = rq.getAuthToken();
        ArrayList<Event> eventArray = new ArrayList<>();

        EventsResponse response = new EventsResponse(null, false);
        String username = null;
        Database db = new Database();
        try {
            Connection conn = db.openConnection();
            /**Get the username here*/
            try {
                AuthTokenDAO authDao = new AuthTokenDAO(conn);
                username = authDao.getUsername(authToken);
                db.closeConnection(true);

            } catch (DataAccessException error) {
                db.closeConnection(false);
                error.printStackTrace();
            }

            /**See if the username is valid*/
            if (username == null) {
                throw new Exception("ERROR: No User Associated");
            }

            /**If it's valid then get the array*/
            try {
                EventDAO eDao = new EventDAO(db.openConnection());
                eventArray = eDao.getEvents(username);
                db.closeConnection(true);

            } catch (DataAccessException error) {
                db.closeConnection(false);
                error.printStackTrace();
            }


            response = new EventsResponse(eventArray, true);

        } catch (Exception e) {

            response = new EventsResponse(false, e.getMessage());
            e.printStackTrace();
        }

        return response;
    }


}
