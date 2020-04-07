package com.example.server.service;

import com.example.server.DAO.*;
import com.example.server.jsonTempClass.Location;
import com.example.server.jsonTempClass.Locations;
import com.example.server.jsonTempClass.Names;
import com.example.shared.model.Event;
import com.example.shared.model.Person;
import com.example.shared.model.User;
import com.example.shared.responses.FillResponse;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import static java.lang.Float.parseFloat;

/**
 * add people and events into the database for a certain user
 */
public class FillService {


    int personCounter;
    int eventCounter;
    String username;
    int generation;
    Locations Locations;
    ArrayList<String> eventType;

    int eventyear = 1980; // the start off default year
    int timeGap = 30; // gap for the ancestors
    String thisUserId;
    String currentEvent;

    Names fnames;
    Names mnames;
    Names snames;

    public FillService() {
    }

    /**
     * fill is the root recursion function and works as a initializer for the class.
     * It creates the data member for the user and then call create ancestor for the user
     *
     * @param username
     * @param generation
     * @param Locations
     * @param fnames
     * @param mnames
     * @param snames
     * @return
     */
    public FillResponse fill(String username, int generation, Locations Locations,
                             Names fnames, Names mnames, Names snames) {
        this.username = username;
        this.Locations = Locations;
        this.fnames = fnames;
        this.mnames = mnames;
        this.snames = snames;
        this.generation = generation;
        personCounter = 1;
        eventCounter = 0;
        eventType = new ArrayList<>();
        eventType.add("birth");
        eventType.add("baptism");
        eventType.add("death");
        currentEvent = new String();


        String father_id = UUID.randomUUID().toString(); // this is for the father to create
        String mother_id = UUID.randomUUID().toString(); // mother to create
        FillResponse rp;

        try {

            deletePastUserData();
            createUser(father_id, mother_id);
            // create mom and dad
            Location parentMarriageLocation = createLocation();

            fillRecursionMale(0, father_id, mother_id, eventyear, parentMarriageLocation);
            fillRecursionFemale(0, mother_id, father_id, eventyear, parentMarriageLocation);
            String message = "Successfully added " + personCounter + " persons and "
                    + eventCounter + " events to the database.";
            rp = new FillResponse(message, true);

        } catch (Exception e) {
            String message = e.getMessage();
            rp = new FillResponse(message, false);
            e.printStackTrace();
        }
        return rp;
    }

    /**
     * If the data associated with the user exist before, delete it.
     */
    private void deletePastUserData() throws Exception {
        Database db = new Database();
        Connection conn = db.openConnection();
        try {
            EventDAO eventDAO = new EventDAO(conn);
            eventDAO.delete(username);

            PersonDAO personDAO = new PersonDAO(conn);
            personDAO.delete(username);

            db.closeConnection(true);
        } catch (DataAccessException dataError) {
            db.closeConnection(false);
        }
    }

    /**
     * Create user and ID and associated birth event
     */
    private void createUser(String father_id, String mother_id) throws Exception {
        Database db = new Database();
        try {
            Connection conn = db.openConnection();
            User user = null;
            try {
                UserDAO userDAO = new UserDAO(conn);
                user = userDAO.getUser(username);
                db.closeConnection(true);
            } catch (DataAccessException dataError) {
                db.closeConnection(false);
            }

            if (user == null) {
                throw new Exception("Error: User does not exist.");
            } else {
                Random random = new Random();//random year for the user to set for event

                thisUserId = user.getPersonID(); // make sure the UserId won't be set as death

                //create birth event for the User
                Location tempL = createLocation();
                Event birth = new Event(UUID.randomUUID().toString(), username, user.getPersonID(),
                        parseFloat(tempL.getLatitude()), parseFloat(tempL.getLongitude()),
                        tempL.getCountry(), tempL.getCity(), "birth", 2000 - random.nextInt(30));

                try {
                    conn = db.openConnection();
                    EventDAO eventDAO = new EventDAO(conn);
                    eventDAO.insert(birth);
                    eventCounter++;
                    db.closeConnection(true);
                } catch (DataAccessException error) {
                    db.closeConnection(false);
                }

                //create person for the User
                Person person = new Person(user.getPersonID(), username, user.getFirstName()
                        , user.getLastName(), user.getGender(), father_id, mother_id, "");
                try {
                    conn = db.openConnection();
                    PersonDAO personDAO = new PersonDAO(conn);
                    personDAO.addPerson(person);
                    db.closeConnection(true);
                } catch (DataAccessException error) {
                    db.closeConnection(false);
                }
            }

        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * fill a male for the last person.
     *
     * @param currentGeneration
     * @param person_id
     * @param spouse_id
     * @param marriageYear
     * @throws Exception
     */
    private void fillRecursionMale(int currentGeneration, String person_id, String spouse_id, int marriageYear, Location location) throws Exception {
        Database db = new Database();
        personCounter++;
        try {


            String male = createMale();
            String surname = createSurname();

            Person temp = new Person(person_id, username, male, surname, "m"); // default person

            temp.setSpouseID(spouse_id);
            marriageYear = fillEvents(person_id, marriageYear, location);

            if (currentGeneration < generation - 1) {
                String father_id = UUID.randomUUID().toString(); //father to create
                String mother_id = UUID.randomUUID().toString(); // mother to create
                temp.setFatherID(father_id);
                temp.setMotherID(mother_id);

                currentGeneration++;
                Location tempLocation = createLocation();
                fillRecursionMale(currentGeneration, father_id, mother_id, marriageYear, tempLocation);
                fillRecursionFemale(currentGeneration, mother_id, father_id, marriageYear, tempLocation);
            }
            PersonDAO personDAO = new PersonDAO(db.openConnection());

            personDAO.addPerson(temp);
            db.closeConnection(true);

        } catch (Exception e) {
            db.closeConnection(false);
            e.printStackTrace();
            throw e;
        }


    }

    /**
     * same as fillRecursionMale but for females
     *
     * @param currentGeneration
     * @param person_id
     * @param spouse_id
     * @param marriageYear
     * @throws Exception
     */
    private void fillRecursionFemale(int currentGeneration, String person_id, String spouse_id, int marriageYear, Location location) throws Exception {
        Database db = new Database();
        try {
            personCounter++;

            PersonDAO personDAO = new PersonDAO(db.openConnection());
            String female = createFemale();
            String surname = createSurname();


            Person temp = new Person(person_id, username, female, surname, "f");
            temp.setSpouseID(spouse_id);


            marriageYear = fillEvents(person_id, marriageYear, location);


            if (currentGeneration < generation - 1) {
                String father_id = UUID.randomUUID().toString();
                String mother_id = UUID.randomUUID().toString();
                temp.setFatherID(father_id);
                temp.setMotherID(mother_id);
                currentGeneration++;
                Location tempLocation = createLocation();
                fillRecursionMale(currentGeneration, father_id, mother_id, marriageYear, tempLocation);
                fillRecursionFemale(currentGeneration, mother_id, father_id, marriageYear, tempLocation);
            }

            personDAO.addPerson(temp);
            db.closeConnection(true);
        } catch (Exception e) {
            db.closeConnection(false);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * fill the event for a certain person Id, It is determined by the marriageYear of the couple
     *
     * @param person_id
     * @return
     * @throws Exception
     */
    private int fillEvents(String person_id, int marriageYear, Location marriageLocation) throws Exception {
        eventCounter += 3;
        Random random = new Random();
        try {


            int birthGap = random.nextInt(5);

            int birthYear = marriageYear - birthGap - 20;

            int deathYear = marriageYear + random.nextInt(40);


            Location tempL = createLocation();
            Event birth = new Event(UUID.randomUUID().toString(), username, person_id,
                    parseFloat(tempL.getLatitude()), parseFloat(tempL.getLongitude()),
                    tempL.getCountry(), tempL.getCity(), "birth", birthYear);


            tempL = createLocation();
            Event death = new Event(UUID.randomUUID().toString(), username, person_id,
                    parseFloat(tempL.getLatitude()), parseFloat(tempL.getLongitude()),
                    tempL.getCountry(), tempL.getCity(), "death", deathYear);


            Event marriage = new Event(UUID.randomUUID().toString(), username, person_id,
                    parseFloat(marriageLocation.getLatitude()), parseFloat(marriageLocation.getLongitude()),
                    marriageLocation.getCountry(), marriageLocation.getCity(), "marriage", marriageYear);


            Database db = new Database();
            /**Open connection here*/
            try {
                Connection conn = db.openConnection();
                EventDAO eventDAO = new EventDAO(conn);
                eventDAO.insert(birth);
                eventDAO.insert(death);
                eventDAO.insert(marriage);
                db.closeConnection(true);
            } catch (DataAccessException error) {
                db.closeConnection(false);
            }

        } catch (Exception e) {

            e.printStackTrace();
            throw e;
        }

        return marriageYear - 30 - random.nextInt(5);
    }


    /**
     * randomly create a male name
     *
     * @return
     */
    private String createMale() {
        Random random = new Random();
        int mValue = random.nextInt(mnames.getData().size());
        String male = mnames.getData().get(mValue);
        return male;
    }

    /**
     * randomly create a surname
     *
     * @return
     */
    private String createSurname() {
        Random random = new Random();
        int sValue = random.nextInt(snames.getData().size());
        String surname = snames.getData().get(sValue);
        return surname;
    }

    /**
     * randomly create a female name
     *
     * @return
     */
    private String createFemale() {
        Random random = new Random();
        int sValue = random.nextInt(fnames.getData().size());
        String fname = fnames.getData().get(sValue);
        return fname;
    }

    /**
     * randomly create a location
     *
     * @return
     */
    private Location createLocation() {
        Random random = new Random();
        int lValue = random.nextInt(Locations.getData().size());
        Location tempLocation = Locations.getData().get(lValue);
        return tempLocation;
    }


}
