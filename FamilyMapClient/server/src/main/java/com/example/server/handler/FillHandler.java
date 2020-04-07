package com.example.server.handler;

import com.example.server.DAO.Database;
import com.example.server.DAO.EventDAO;
import com.example.server.DAO.PersonDAO;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.example.server.jsonTempClass.Locations;
import com.example.server.jsonTempClass.Names;
import com.example.shared.responses.FillResponse;
import com.example.server.service.FillService;

import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;

/**
 * fill handler, handle fill function
 */

public class FillHandler implements HttpHandler {

    private int generationCount;
    private Decode dc;

    private Locations location;
    private Names maleNames, femaleNames, lastName;

    public FillHandler() {
    }


    /**
     * handle the fill funciton
     *
     * @param exchange
     * @throws IOException
     */
    public void handle(HttpExchange exchange) throws IOException {

        System.out.print("fillHandler\n");
        boolean success = false;

        Encode ec = new Encode();
        dc = new Decode();
        Database db = new Database();
        try {

            Reader locationReader = new FileReader("familymapserver/json/Locations.json");
            location = dc.decodeLocation(locationReader);

            Reader nameReader = new FileReader("familymapserver/json/fnames.json");
            femaleNames = dc.decodeNames(nameReader);

            Reader nameReader2 = new FileReader("familymapserver/json/mnames.json");
            maleNames = dc.decodeNames(nameReader2);

            Reader nameReader3 = new FileReader("familymapserver/json/snames.json");
            lastName = dc.decodeNames(nameReader3);

            if (exchange.getRequestMethod().toLowerCase().equals("post")) {


                String theURI = exchange.getRequestURI().toString();
                String[] personString = theURI.split("/");
                String username = personString[2];

                if (personString.length == 4) //different type of fill
                {

                    generationCount = Integer.parseInt(personString[3]);

                    FillService service = new FillService();
                    FillResponse rp = service.fill(username, generationCount,
                            location, femaleNames, maleNames, lastName);

                    String response = ec.encodeFill(rp);

                    if (rp.isSuccess()) {
                        OutputStream resBody = exchange.getResponseBody();
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                        ec.writeString(response, resBody);

                        exchange.getResponseBody().close();

                    } else {

                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        OutputStream resBody = exchange.getResponseBody();
                        ec.writeString(response, resBody);
                        exchange.getResponseBody().close();
                    }
                } else if (personString.length == 3) // diffenet type of fill
                {

                    FillService service = new FillService();
                    FillResponse rp = service.fill(username, 4,
                            location, femaleNames, maleNames, lastName);
                    String response = ec.encodeFill(rp);
                    if (rp.isSuccess()) {

                        OutputStream resBody = exchange.getResponseBody();
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                        ec.writeString(response, resBody);

                        exchange.getResponseBody().close();
                    } else {

                        rp = new FillResponse();
                        rp.setMessage("ERROR");
                        response = ec.encodeFill(rp);
                        OutputStream resBody = exchange.getResponseBody();
                        resBody = exchange.getResponseBody();
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
                        ec.writeString(response, resBody);

                        exchange.getResponseBody().close();

                    }

                }
            } else {
                throw new Exception("Has to be POST");
            }

        } catch (Exception e) {

            FillResponse rp = new FillResponse();
            rp.setMessage("ERROR");
            String response = ec.encodeFill(rp);

            OutputStream resBody = exchange.getResponseBody();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            ec.writeString(response, resBody);

            exchange.getResponseBody().close();
            e.printStackTrace();
        }
        exchange.close();

    }
}
