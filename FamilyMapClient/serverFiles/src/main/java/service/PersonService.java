package service;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import model.Person;
import requests.PersonRequest;
import responses.PersonResponse;
import responses.PersonsResponse;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * get the person Service
 */
public class PersonService {
    public PersonService() {
    }

    /**
     * get the person
     *
     * @param rq
     * @return
     */
    public PersonResponse getPerson(PersonRequest rq) {

        String authToken = rq.getAuthToken();
        String personID = rq.getPersonId();


        PersonResponse response = new PersonResponse();
        String username = null;
        Database db = new Database();
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO authDao = new AuthTokenDAO(conn);
            PersonDAO pDao = new PersonDAO(conn);

            username = authDao.getUsername(authToken);
            Person pModel = pDao.getPerson(personID);

            /**Do some error checking here*/
            if (pModel == null)
                throw new Exception("ERROR: Person not found.");


            if (!pModel.getAssociatedUsername().equals(username)) {
                throw new Exception("ERROR: Not your ancestor.");
            }


            response = new PersonResponse(pModel.getPersonID(),
                    pModel.getAssociatedUsername(), pModel.getFirstName(),
                    pModel.getLastName(), pModel.getGender(),
                    pModel.getFatherID(), pModel.getMotherID(),
                    pModel.getSpouseID(), null, true);
            db.closeConnection(true);

        } catch (Exception e) {
            try {
                db.closeConnection(false);
            } catch (DataAccessException closeError) {
                closeError.printStackTrace();
            }
            response = new PersonResponse(null, null, null, null, null, null, null, null, e.getMessage(), false);
            e.printStackTrace();
        }

        return response;
    }

    /**
     * return an array of the Person response objects
     *
     * @param rq
     * @return
     */
    public PersonsResponse getPeople(PersonRequest rq) {

        String authToken = rq.getAuthToken();
        ArrayList<Person> personArray = new ArrayList<>();

        PersonsResponse response = new PersonsResponse();
        String username = null;
        Database db = new Database();
        try {
            Connection conn = db.openConnection();
            /**Get the user first*/
            try {
                AuthTokenDAO authDao = new AuthTokenDAO(conn);
                username = authDao.getUsername(authToken);
                PersonDAO pDao = new PersonDAO(conn);

                personArray = pDao.getPeople(username);
                db.closeConnection(true);
            } catch (DataAccessException error) {
                db.closeConnection(false);
                error.printStackTrace();
            }

            /**See if the username is valid*/
            if (username == null) {
                throw new Exception("ERROR: No User Associated.");
            }


            /**If it's valid then get the array*/
            try {
                PersonDAO pDao = new PersonDAO(db.openConnection());
                personArray = pDao.getPeople(username);
                db.closeConnection(true);
            } catch (DataAccessException error) {
                db.closeConnection(false);
                error.printStackTrace();
            }


            response = new PersonsResponse(personArray, true);

        } catch (Exception e) {

            response = new PersonsResponse(false, e.getMessage());
            e.printStackTrace();
        }

        return response;
    }


}
