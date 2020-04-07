package com.example.shared.responses;

import java.util.Objects;

/**
 * return the Result for login
 */
public class LoginResponse {
    private String authToken;
    private String userName;
    private String personID;
    private String message;
    private boolean success;

    public LoginResponse() {
    }

    /**
     * non-default constructor
     *
     * @param authToken
     * @param userName
     * @param personID
     */
    public LoginResponse(String authToken, String userName, String personID, boolean success) {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
        this.success = success;
    }

    public LoginResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    /**
     * Since the Login function will randomly generate authToken ID, so we are going to check everything other than auth
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginResponse)) return false;
        LoginResponse that = (LoginResponse) o;
        return Objects.equals(userName, that.userName) &&
                Objects.equals(personID, that.personID) &&
                Objects.equals(message, that.message) && Objects.equals(success, that.success);
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
