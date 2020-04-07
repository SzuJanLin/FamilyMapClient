package com.example.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.example.shared.responses.ClearResponse;
import com.example.server.service.ClearService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * clear handler, handler the clear call
 */
public class ClearHandler implements HttpHandler {
    Encode ec;
    ClearResponse rp;
    String response;
    OutputStream resBody;

    @Override

    /**
     * handle the clear call from the server
     */
    public void handle(HttpExchange exchange) throws IOException {
        System.out.print("clearHandler");
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {


                ec = new Encode();
                ClearService cl = new ClearService();
                rp = cl.clear();


                response = ec.encodeClear(rp);

                /** We want to see if the response is successful or not*/
                if (rp.isSuccess()) {
                    resBody = exchange.getResponseBody();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    ec.writeString(response, resBody);
                    exchange.getResponseBody().close();

                } else {
                    rp.setMessage("ERROR: Clear is not successful.");
                    rp.setSuccess(false);
                    response = ec.encodeClear(rp);
                    resBody = exchange.getResponseBody();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    ec.writeString(response, resBody);
                    exchange.getResponseBody().close();
                }

            }
        } catch (Exception e) {

            rp.setMessage(e.getMessage());
            rp.setSuccess(false);
            response = ec.encodeClear(rp);
            resBody = exchange.getResponseBody();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

            ec.writeString(response, resBody);

            exchange.getResponseBody().close();
        }
        exchange.close();

    }


}
