package com.example.shared.model;

import java.util.Objects;

/**
 * authToken class , a model for authToken
 */
public class AuthToken {
    private String authToken;
    private String userName;

    public AuthToken(String authToken, String userName) {
        this.authToken = authToken;
        this.userName = userName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthToken)) return false;
        AuthToken authToken1 = (AuthToken) o;
        return Objects.equals(authToken, authToken1.authToken) &&
                Objects.equals(userName, authToken1.userName);
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
}
