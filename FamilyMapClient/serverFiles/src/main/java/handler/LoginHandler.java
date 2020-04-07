package handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import requests.LoginRequest;
import responses.LoginResponse;
import service.LoginService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;

/**
 * handle login
 */
public class LoginHandler implements HttpHandler {

    Encode ec;
    LoginResponse rp;
    OutputStream resBody;
    String response;

    /**
     * handle login
     *
     * @param exchange
     * @throws IOException
     */
    public void handle(HttpExchange exchange) throws IOException {
        System.out.print("loginHandler");
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                Gson gson = new Gson();

                ec = new Encode();
                LoginService service = new LoginService();

                Reader in = new InputStreamReader(exchange.getRequestBody());
                JsonObject jsonObject = gson.fromJson(in, JsonObject.class);
                LoginRequest request = gson.fromJson(jsonObject, LoginRequest.class);


                System.out.print(request.getPassword());

                rp = service.login(request);

                if (rp.isSuccess()) {
                    response = ec.encodeLogin(rp);


                    resBody = exchange.getResponseBody();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                    ec.writeString(response, resBody);

                    exchange.getResponseBody().close();
                    System.out.print("successful");

                } else {

                    System.out.print("ERROR\n");
                    rp.setMessage("ERROR");
                    response = ec.encodeLogin(rp);
                    resBody = exchange.getResponseBody();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                    ec.writeString(response, resBody);

                    exchange.getResponseBody().close();
                }

            } else {
                System.out.print("not post");
            }
        } catch (Exception e) {
            System.out.print("EXCEPTION");
            System.out.print(e.getMessage());
            rp.setMessage("ERROR");
            response = ec.encodeLogin(rp);
            resBody = exchange.getResponseBody();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

            ec.writeString(response, resBody);

            exchange.getResponseBody().close();
            e.printStackTrace();
        }
        exchange.close();

    }
}


