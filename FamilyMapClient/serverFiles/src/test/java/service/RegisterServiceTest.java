package service;

import DAO.Database;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.RegisterRequest;
import responses.RegisterResponse;


import static org.junit.jupiter.api.Assertions.*;
public class RegisterServiceTest {

        private Database db;


        @BeforeEach
        public void setUp() throws Exception{
            db = new Database();

        }

        @AfterEach
        public void tearDown() throws Exception {
            //here we can get rid of anything from our tests we don't want to affect the rest of our program
            //lets clear the tables so that any data we entered for testing doesn't linger in our files

            db.openConnection();
            db.clearTables();
            db.closeConnection(true);
        }

        @Test
        public void RegisterPass() throws Exception{
            /** we don't need anything in the database for registration. Just pass in a request*/
            RegisterRequest registerRequest = new RegisterRequest("jordan","12345","1232@ere",
                    "Jordan","Lin","m");

            RegisterService registerService = new RegisterService();

            RegisterResponse output = registerService.Register(registerRequest);
            RegisterResponse registerResponse = new RegisterResponse("not feasible",
                    "jordan","not feasible",true);

            assertEquals(output.getMessage(),registerResponse.getMessage());
            assertEquals(output.getUserName(),registerResponse.getUserName());
        }

        /**Pass in incomplete Data*/
        @Test
        public void RegisterFailNull() throws Exception{
        /** we don't need anything in the database for registration. Just pass in a request*/
        RegisterRequest registerRequest = new RegisterRequest(null,null,"1232@ere",
                "Jordan","Lin","m");

        RegisterService registerService = new RegisterService();

        RegisterResponse output = registerService.Register(registerRequest);
        RegisterResponse registerResponse = new RegisterResponse("ERROR: context cannot be null",false);

        assertEquals(output,registerResponse);
        }

    @Test
    public void RegisterFailSameUser() throws Exception{
        /** we don't need anything in the database for registration. Just pass in a request*/
        RegisterRequest registerRequest = new RegisterRequest("jordan","12345","1232@ere",
                "Jordan","Lin","m");

        RegisterService registerService = new RegisterService();
        registerService.Register(registerRequest);
        RegisterResponse output = registerService.Register(registerRequest);
        RegisterResponse registerResponse = new RegisterResponse("ERROR: This username was registered",false);

        assertEquals(output,registerResponse);
    }

}
