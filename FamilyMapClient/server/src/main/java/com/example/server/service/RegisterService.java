package com.example.server.service;

import com.example.server.DAO.AuthTokenDAO;
import com.example.server.DAO.Database;
import com.example.server.DAO.UserDAO;
import com.example.server.handler.Decode;
import com.example.server.jsonTempClass.Locations;
import com.example.server.jsonTempClass.Names;
import com.example.shared.model.AuthToken;
import com.example.shared.model.User;
import com.example.shared.requests.RegisterRequest;
import com.example.shared.responses.RegisterResponse;

import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.util.UUID;

/**
 * register the person
 */
public class RegisterService {

    public RegisterService() {
    }

    String username;

    /**
     * register for the person
     *
     * @param rq
     * @return
     */
    public RegisterResponse Register(RegisterRequest rq) {

        String authToken = null;
        String personID = null;
        RegisterResponse response = new RegisterResponse();
        Database db = new Database();

        try {
            Connection conn = db.openConnection();
            //create authToken for the register user, login for the user
            UserDAO uDao = new UserDAO(conn);
            authToken = UUID.randomUUID().toString();
            personID = UUID.randomUUID().toString();


            username = rq.getUserName();
            User userTemp = new User(rq.getUserName(), rq.getPassword(), rq.getEmail(),
                    rq.getFirstName(), rq.getLastName(), rq.getGender(), personID);


            User test = uDao.getUser(rq.getUserName());
            // check if user have null
            if (test != null) {
                throw new Exception("ERROR: This username was registered");
            }

            // cannot have null context
            if (rq.getUserName() == null) {
                throw new Exception("ERROR: context cannot be null");
            }

            if (rq.getGender().equals("m") || rq.getGender().equals("f")) {

                response = new RegisterResponse(authToken, rq.getUserName(), personID, true);
                uDao.addUser(userTemp);
                db.closeConnection(true);
                generateFamily();
                //Login
                AuthToken authTemp = new AuthToken(authToken, rq.getUserName());

                conn = db.openConnection();
                AuthTokenDAO authDao = new AuthTokenDAO(conn);
                authDao.addAuthToken(authTemp);
                db.closeConnection(true);
            } else {
                throw new Exception("ERROR: please specify your gender");
            }


        } catch (Exception e) {
            try {
                db.closeConnection(false);
            } catch (Exception closeError) {
                closeError.printStackTrace();
            }
            e.printStackTrace();

            response = new RegisterResponse(e.getMessage(), false);
        }
        return response;

    }

    /**
     * generate family for the register person, call the fill function
     */
    private void generateFamily() {
        try {
            Locations location;
            Names femaleNames, maleNames, lastName;
            Decode dc = new Decode();

            Reader locationReader = new FileReader("familymapserver/json/Locations.json");
            location = dc.decodeLocation(locationReader);

            Reader nameReader = new FileReader("familymapserver/json/fnames.json");
            femaleNames = dc.decodeNames(nameReader);

            Reader nameReader2 = new FileReader("familymapserver/json/mnames.json");
            maleNames = dc.decodeNames(nameReader2);

            Reader nameReader3 = new FileReader("familymapserver/json/snames.json");
            lastName = dc.decodeNames(nameReader3);


            FillService fillService = new FillService();
            fillService.fill(username, 4, location, femaleNames, maleNames, lastName);
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
