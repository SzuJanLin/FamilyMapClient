package com.example.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.example.shared.requests.LoadRequest;
import com.example.shared.responses.LoadResponse;
import com.example.server.service.LoadService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;

/**
 * handle load
 */
public class loadHandler implements HttpHandler {
    public loadHandler() {
    }

    String response;
    OutputStream resBody;
    Encode ec;
    LoadResponse rp;

    /**
     * handle load
     *
     * @param exchange
     * @throws IOException
     */
    public void handle(HttpExchange exchange) throws IOException {

        System.out.print("LoadHandler");
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                ec = new Encode();
                Decode dc = new Decode();
                LoadService service = new LoadService();

                Reader in = new InputStreamReader(exchange.getRequestBody());
                LoadRequest request = dc.decodeLoad(in);
                rp = service.load(request);

                if (rp.isSuccess()) {
                    response = ec.encodeLoad(rp);

                    resBody = exchange.getResponseBody();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    ec.writeString(response, resBody);

                    exchange.getResponseBody().close();
                } else {

                    rp.setMessage("ERROR");
                    response = ec.encodeLoad(rp);
                    resBody = exchange.getResponseBody();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    ec.writeString(response, resBody);
                    exchange.getResponseBody().close();
                }

            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            rp.setMessage("ERROR");
            response = ec.encodeLoad(rp);
            resBody = exchange.getResponseBody();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            ec.writeString(response, resBody);


            exchange.getResponseBody().close();
            e.printStackTrace();
        }
        exchange.close();

    }
}

