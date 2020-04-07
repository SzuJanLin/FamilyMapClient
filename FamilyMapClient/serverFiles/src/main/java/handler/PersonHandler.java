package handler;

import DAO.AuthTokenDAO;
import DAO.Database;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import requests.PersonRequest;
import responses.PersonResponse;
import responses.PersonsResponse;
import service.PersonService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.Connection;

/**
 * handle person
 */
public class PersonHandler implements HttpHandler {
    /**
     * handle person
     *
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.print("personHandler");
        Gson gson = new Gson();
        Encode ec = new Encode();
        Decode dc = new Decode();
        PersonService service = new PersonService();
        Database db = new Database();
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                Headers reqHeaders = exchange.getRequestHeaders();

                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");

                    Connection conn = db.openConnection();
                    AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);

                    if (authTokenDAO.getUsername(authToken) == null)//assert
                    {
                        db.closeConnection(false);
                        throw new Exception("ERROR: No AuthToken provided.");
                    }
                    db.closeConnection(true);


                    String theURI = exchange.getRequestURI().toString();
                    String[] personString = theURI.split("/");

                    if (personString.length == 3) {
                        String person_id = personString[2];
                        PersonRequest request = new PersonRequest(authToken, person_id);

                        PersonResponse rp = service.getPerson(request);


                        String response = ec.encodePerson(rp);
                        OutputStream resBody = exchange.getResponseBody();

                        if (rp.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                        } else {

                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        }

                        ec.writeString(response, resBody);
                        exchange.getResponseBody().close();
                    } else if (personString.length == 2) {

                        PersonRequest request = new PersonRequest(authToken, "");
                        PersonsResponse rp = service.getPeople(request);


                        String response = ec.encodePeople(rp);

                        OutputStream resBody = exchange.getResponseBody();


                        if (rp.isSuccess()) {

                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                        }
                        ec.writeString(response, resBody);

                        exchange.getResponseBody().close();
                    }
                }
            }
        } catch (Exception e) {
            PersonResponse rp = new PersonResponse();
            rp.setMessage(e.getMessage());
            String response = ec.encodePerson(rp);

            OutputStream resBody = exchange.getResponseBody();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            ec.writeString(response, resBody);

            exchange.getResponseBody().close();
            e.printStackTrace();
        }
        exchange.close();
    }

}
