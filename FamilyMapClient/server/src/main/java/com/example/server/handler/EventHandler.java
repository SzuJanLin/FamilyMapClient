package com.example.server.handler;

import com.example.server.DAO.AuthTokenDAO;
import com.example.server.DAO.Database;
import com.example.shared.responses.EventsResponse;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.example.shared.requests.EventRequest;
import com.example.shared.responses.EventResponse;
import com.example.server.service.EventService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.Connection;

/**
 * handle the event
 */
public class EventHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Encode ec = new Encode();
        System.out.print("eventHandler");
        Database db = new Database();
        try {
            Connection conn = db.openConnection();
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {

                EventService service = new EventService();
                Headers reqHeaders = exchange.getRequestHeaders();

                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");

                    AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);

                    /** Try to see if AuthToken is there.*/
                    if (authTokenDAO.getUsername(authToken) == null) {
                        db.closeConnection(false);
                        throw new Exception("ERROR: No AuthToken provided.");
                    }
                    db.closeConnection(true);

                    String theURI = exchange.getRequestURI().toString();
                    String[] eventString = theURI.split("/");

                    if (eventString.length == 3) {
                        String event_id = eventString[2];

                        EventRequest request = new EventRequest(authToken, event_id);

                        EventResponse rp = service.getEvent(request);

                        if (rp.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        }

                        /**From here we send the resBody back to the client*/
                        String response = ec.encodeEvent(rp);
                        OutputStream resBody = exchange.getResponseBody();
                        ec.writeString(response, resBody);
                        exchange.getResponseBody().close();
                    } else if (eventString.length == 2) {

                        EventRequest request = new EventRequest(authToken, "");
                        EventsResponse rp = service.getEvents(request);
                        String response = ec.encodeEvents(rp);
                        OutputStream resBody = exchange.getResponseBody();
                        if (rp.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        }
                        /**From here we send the resBody back to the client*/
                        ec.writeString(response, resBody);
                        exchange.getResponseBody().close();
                    }
                }
            }
        } catch (Exception e) {
            EventResponse rp = new EventResponse();
            rp.setMessage(e.getMessage());
            String response = ec.encodeEvent(rp);

            OutputStream resBody = exchange.getResponseBody();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            ec.writeString(response, resBody);

            exchange.getResponseBody().close();
            e.printStackTrace();
        }
        exchange.close();

    }


}


