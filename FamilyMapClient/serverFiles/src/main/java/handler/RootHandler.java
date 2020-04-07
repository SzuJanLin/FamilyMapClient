package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;


/**
 * root handler
 */
public class RootHandler implements HttpHandler {

    /**
     * root handler function
     *
     * @param exchange
     * @throws IOException
     */
    public void handle(HttpExchange exchange) throws IOException {
        System.out.print("default handler\n");
        try {

            if (exchange.getRequestMethod().toLowerCase().equals("get")) {


                String get = exchange.getRequestURI().getPath();
                if (get.length() == 0 || get.equals("/")) {
                    get = "/index.html";
                }
                File Result = new File("newWeb/familymapserver/web/" + get);

                if (!Result.isFile()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    get = "html/404.html";
                    Result = new File("newWeb/familymapserver/web/" + get);
                    Files.copy(Result.toPath(), exchange.getResponseBody());
                    exchange.getResponseBody().close();
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    Files.copy(Result.toPath(), exchange.getResponseBody());
                    exchange.getResponseBody().close();

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


