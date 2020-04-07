package com.example.familymapclient;

import com.example.shared.requests.LoginRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Proxy {

    public String getConnection(URL url, String authToken){
        String respData = null;
        try {

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("GET");
            http.setDoOutput(false);

            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");

            http.connect();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                // Get the input stream containing the HTTP response body
                InputStream respBody = http.getInputStream();
                respData = readString(respBody);
            }
            else {

                InputStream respBody = http.getInputStream();
                respData = readString(respBody);
                System.out.println("ERROR: " + http.getResponseMessage());
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }

        return respData;
    }




    public String postConnection(URL url, String request) throws IOException, BadResponseCodeException {
        String respData = null;
        try {

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.addRequestProperty("Accept", "application/json");
            connection.connect();

            OutputStream reqBody = connection.getOutputStream();
            writeString(request, reqBody);
            reqBody.close();


            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // The HTTP response status code indicates success,
                // so print a success message
                InputStream respBody = connection.getInputStream();
                // Extract JSON data from the HTTP response body
                respData = readString(respBody);
                System.out.println("Route successfully claimed.");
            } else {
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                System.out.println("ERROR: " + connection.getResponseMessage());
                InputStream respBody = connection.getInputStream();
                // Extract JSON data from the HTTP response body
                respData = readString(respBody);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return respData;
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }




    public static class BadResponseCodeException extends Exception {
        private final int responseCode;
        private final URL url;

        BadResponseCodeException(int responseCode, URL url) {
            super(String.format("Received response code (%d) for url (%s)", responseCode, url.toString()));
            this.responseCode = responseCode;
            this.url = url;
        }

        public int getResponseCode() {
            return responseCode;
        }

        public URL getUrl() {
            return url;
        }
    }

}
