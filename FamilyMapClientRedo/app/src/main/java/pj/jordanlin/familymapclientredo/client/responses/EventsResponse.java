package pj.jordanlin.familymapclientredo.client.responses;

import java.util.ArrayList;

public class EventsResponse {
    private ArrayList<EventResponse> eventResponseArrayList;

    public  EventsResponse(){}
    public EventsResponse(ArrayList<EventResponse> eventResponseArrayList) {
        this.eventResponseArrayList = eventResponseArrayList;
    }

    public ArrayList<EventResponse> getEventResponseArrayList() {
        return eventResponseArrayList;
    }

    public void setEventResponseArrayList(ArrayList<EventResponse> eventResponseArrayList) {
        this.eventResponseArrayList = eventResponseArrayList;
    }
}
