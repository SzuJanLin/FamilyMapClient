package com.example.shared.responses;

import com.example.shared.model.Person;

import java.util.ArrayList;
import java.util.Objects;

public class PersonsResponse {

    private ArrayList<Person> data;
    private boolean success;
    private String message;

    public PersonsResponse(ArrayList<Person> data, boolean success) {
        this.data = data;
        this.success = success;
    }

    public PersonsResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public PersonsResponse() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonsResponse)) return false;
        PersonsResponse that = (PersonsResponse) o;
        return success == that.success &&
                Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, success);
    }

    public ArrayList<Person> getData() {
        return data;
    }

    public void setData(ArrayList<Person> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
