package com.example.server.jsonTempClass;

import com.example.shared.responses.PersonResponse;

import java.util.ArrayList;

public class PeopleResponseJSON {
    private ArrayList<PersonResponse> personResponses;

    public ArrayList<PersonResponse> getPersonResponses() {
        return personResponses;
    }

    public void setPersonResponses(ArrayList<PersonResponse> personResponses) {
        this.personResponses = personResponses;
    }
}
