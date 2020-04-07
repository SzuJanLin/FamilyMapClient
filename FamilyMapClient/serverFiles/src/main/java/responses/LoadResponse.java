package responses;

import java.util.Objects;

/**
 * response body for load function
 */
public class LoadResponse {
    private String message;
    private boolean success;

    public LoadResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, success);
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
        if (!(o instanceof LoadResponse)) return false;
        LoadResponse that = (LoadResponse) o;
        return Objects.equals(message, that.message);
    }


    public LoadResponse() {
    }

    public LoadResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
