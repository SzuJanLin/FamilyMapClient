package com.example.shared.responses;

import java.util.Objects;

/**
 * response body for clear function;
 */
public class ClearResponse {
    private String message;
    private boolean success;

    public ClearResponse() {
    }

    public ClearResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, success);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClearResponse)) return false;
        ClearResponse that = (ClearResponse) o;
        return message.equals(that.message);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
