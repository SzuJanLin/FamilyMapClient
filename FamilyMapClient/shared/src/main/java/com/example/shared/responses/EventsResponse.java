package com.example.shared.responses;

import com.example.shared.model.Event;

import java.util.ArrayList;
import java.util.Objects;

public class EventsResponse {

    private ArrayList<Event> data;
    private boolean success;
    private String message;

    public EventsResponse(ArrayList<Event> data, boolean success) {
        this.data = data;
        this.success = success;
    }

    public EventsResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ArrayList<Event> getData() {
        return data;
    }

    public void setData(ArrayList<Event> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventsResponse)) return false;
        EventsResponse that = (EventsResponse) o;
        return success == that.success &&
                Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, success);
    }

}
