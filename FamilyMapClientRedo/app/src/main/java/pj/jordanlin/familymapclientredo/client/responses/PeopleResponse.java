package pj.jordanlin.familymapclientredo.client.responses;

import java.util.ArrayList;

public class PeopleResponse {
    ArrayList<PersonResponse> PeopleResponseArrayList;

    public PeopleResponse(ArrayList<PersonResponse> peopleResponseArrayList) {
        PeopleResponseArrayList = peopleResponseArrayList;
    }

    public  PeopleResponse(){}

    public ArrayList<PersonResponse> getPeopleResponseArrayList() {
        return PeopleResponseArrayList;
    }

    public void setPeopleResponseArrayList(ArrayList<PersonResponse> peopleResponseArrayList) {
        PeopleResponseArrayList = peopleResponseArrayList;
    }
}
