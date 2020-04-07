package com.example.server.handler;

import com.google.gson.Gson;
import com.example.shared.responses.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * encode the response classes to json string
 */
public class Encode {


    /**
     * encode the clear response
     *
     * @param rp
     * @return
     */
    public String encodeClear(ClearResponse rp) {
        Gson gson = new Gson();
        String json = gson.toJson(rp);
        return json;
    }

    /**
     * \
     * encode loginResponse
     *
     * @param rp
     * @return
     */
    public String encodeLogin(LoginResponse rp) {
        Gson gson = new Gson();
        String json = gson.toJson(rp);
        return json;
    }

    /**
     * encode registerResponse
     *
     * @param rp
     * @return
     */
    public String encodeRegister(RegisterResponse rp) {
        Gson gson = new Gson();
        String json = gson.toJson(rp);
        return json;
    }

    /**
     * encode PersonResponse
     *
     * @param rp
     * @return
     */
    public String encodePerson(PersonResponse rp) {
        Gson gson = new Gson();
        String json = gson.toJson(rp);
        return json;
    }

    /**
     * encode array of perosn responses
     *
     * @param rp
     * @return
     */
    public String encodePeople(PersonsResponse rp) {
        Gson gson = new Gson();
        String json = gson.toJson(rp);
        return json;
    }

    /**
     * encode eventResponses
     *
     * @param rp
     * @return
     */
    public String encodeEvent(EventResponse rp) {
        Gson gson = new Gson();
        String json = gson.toJson(rp);
        return json;
    }

    /**
     * encode array of eventResponses
     *
     * @param rp
     * @return
     */
    public String encodeEvents(EventsResponse rp) {
        Gson gson = new Gson();
        String json = gson.toJson(rp);
        return json;
    }

    /**
     * encode fillResponse
     *
     * @param rp
     * @return
     */
    public String encodeFill(FillResponse rp) {
        Gson gson = new Gson();
        String json = gson.toJson(rp);
        return json;
    }

    /**
     * encode LoadResponse
     *
     * @param rp
     * @return
     */
    public String encodeLoad(LoadResponse rp) {
        Gson gson = new Gson();
        String json;
        json = gson.toJson(rp);
        return json;
    }


    /**
     * write outputString to string
     *
     * @param str
     * @param os
     * @throws IOException
     */
    public void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.close();
    }
}
