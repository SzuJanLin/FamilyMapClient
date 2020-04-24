package com.example.familymapclient;


import com.example.shared.requests.LoginRequest;
import com.example.shared.requests.RegisterRequest;
import com.example.shared.responses.LoginResponse;
import com.example.shared.responses.RegisterResponse;
import com.google.gson.Gson;

import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.jupiter.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.net.URL;

public class proxyTest {
    private Proxy proxy= new Proxy();;
    private String serverHost= "192.168.1.150";
    private String serverPort= "8080";
    private String username= "sheila";
    private String password= "parker";
    private String personID= "Sheila_Parker";


    @BeforeEach
    public void setup(){
        proxy = new Proxy();
        serverHost = "192.168.1.150";
        serverPort = "8080";
        username = "sheila";
        password = "parker";
        personID = "Sheila_Parker";
    }

    @AfterEach
    public void tearDown(){

    }



    @Test
    public void loginPass(){
        LoginRequest loginRequest = new LoginRequest(username,password);
        Gson gson = new Gson();
        String loginInString = gson.toJson(loginRequest);
        LoginResponse  response = null;
        try{
            URL url = new URL("http://"+serverHost+":"+serverPort+"/user/login");
            String loginContent = proxy.postConnection(url,loginInString);
            response = gson.fromJson(loginContent, LoginResponse.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        assertEquals(username,response.getUserName());
        assertEquals(personID, response.getPersonID());
    }

    @Test
    public void loginFail(){
        LoginRequest loginRequest = new LoginRequest("notAUserName",password);
        Gson gson = new Gson();
        String loginInString = gson.toJson(loginRequest);
        LoginResponse  response = null;
        try{
            URL url = new URL("http://"+serverHost+":"+serverPort+"/user/login");
            String loginContent = proxy.postConnection(url,loginInString);
            response = gson.fromJson(loginContent, LoginResponse.class);
        }catch (Exception e){
            e.printStackTrace();
            //bad response

        }
        assertNull(response);

    }

    @Test
    public void registerPass(){
        RegisterRequest registerRequest = new RegisterRequest("YESS", "TRUEeD", "man@byu.edu","man","dued","m");
        Gson gson = new Gson();
        String registerInRequest = gson.toJson(registerRequest);
        RegisterResponse response = null;
        try{
            URL url = new URL("http://"+serverHost+":"+serverPort+"/user/register");
            String registerContent = proxy.postConnection(url,registerInRequest);
            response = gson.fromJson(registerContent,RegisterResponse.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        assertTrue(response.isSuccess());

    }

    @Test
    public void registerFail(){
        RegisterRequest registerRequest = new RegisterRequest(username, password, "man@byu.edu","man","dued","m");
        Gson gson = new Gson();
        String registerInRequest = gson.toJson(registerRequest);
        RegisterResponse response = null;
        try{
            URL url = new URL("http://"+serverHost+":"+serverPort+"/user/register");
            String registerContent = proxy.postConnection(url,registerInRequest);
            response = gson.fromJson(registerContent,RegisterResponse.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        assertNull(response);
    }



}
