package com.example.server.handler;

import com.google.gson.Gson;
import com.example.server.jsonTempClass.Locations;
import com.example.server.jsonTempClass.Names;
import com.example.shared.requests.LoadRequest;

import java.io.Reader;

/**
 * decode the objects passed by the server
 */
public class Decode {
    /**
     * decode LoadRequest
     *
     * @param reader
     * @return
     */
    public LoadRequest decodeLoad(Reader reader) {
        Gson gson = new Gson();
        LoadRequest request = gson.fromJson(reader, LoadRequest.class);
        return request;
    }

    /**
     * decode Location request
     *
     * @param reader
     * @return
     */
    public Locations decodeLocation(Reader reader) {
        Gson gson = new Gson();
        Locations request = gson.fromJson(reader, Locations.class);
        return request;
    }

    /**
     * decode names
     *
     * @param reader
     * @return
     */
    public Names decodeNames(Reader reader) {
        Gson gson = new Gson();
        Names request = gson.fromJson(reader, Names.class);
        return request;
    }

}
