package com.example.shared.responses;

import java.util.Objects;

/**
 * Register Result for the service function
 */
public class RegisterResponse {
    private String authToken;
    private String userName;
    private String personID;
    private String message;
    private boolean success;

    public RegisterResponse() {
    }

    public RegisterResponse(String authToken, String userName, String personID, boolean success) {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
        this.success = success;
    }

    public RegisterResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof RegisterResponse)) return false;
        RegisterResponse that = (RegisterResponse) o;
        return success == that.success &&
                Objects.equals(authToken, that.authToken) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(personID, that.personID) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken, userName, personID, message, success);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
