package responses;

import java.util.Objects;

public class FillResponse {
    private String message;
    private boolean success;

    public FillResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FillResponse)) return false;
        FillResponse that = (FillResponse) o;
        return success == that.success &&
                Objects.equals(message, that.message);
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

    public FillResponse() {
    }

    public FillResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
