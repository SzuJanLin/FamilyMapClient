package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import requests.RegisterRequest;
import responses.RegisterResponse;
import service.RegisterService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;

/**
 * handle register
 */
public class RegisterHandler implements HttpHandler {
    public RegisterHandler() {
    }

    private String username;
    RegisterRequest request;
    Encode ec;
    Gson gson;
    RegisterResponse rp;
    String response;

    /**
     * handle register
     *
     * @param exchange
     * @throws IOException
     */
    public void handle(HttpExchange exchange) throws IOException {

        System.out.print("RegisterHandler");
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                gson = new Gson();
                ec = new Encode();

                RegisterService service;
                service = new RegisterService();

                Reader in = new InputStreamReader(exchange.getRequestBody());
                request = gson.fromJson(in, RegisterRequest.class);
                username = request.getUserName();

                rp = service.Register(request);

                if (rp.isSuccess()) {
                    response = ec.encodeRegister(rp);
                    OutputStream resBody = exchange.getResponseBody();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    ec.writeString(response, resBody);


                } else {
                    rp.setMessage("ERROR");
                    response = ec.encodeRegister(rp);
                    OutputStream resBody = exchange.getResponseBody();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    ec.writeString(response, resBody);
                }

            } else {
                throw new Exception("Wrong Method");
            }


        } catch (Exception e) {


            rp.setMessage(e.getMessage());
            response = ec.encodeRegister(rp);

            OutputStream resBody = exchange.getResponseBody();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            ec.writeString(response, resBody);

            e.printStackTrace();
        }

        exchange.getResponseBody().close();
        exchange.close();

    }


}




