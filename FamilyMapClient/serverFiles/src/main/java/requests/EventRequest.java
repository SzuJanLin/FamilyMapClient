package requests;

/**
 * a request form to the service
 */
public class EventRequest {
    private String AuthToken;
    private String event_id;


    /**
     * non default constructor
     *
     * @param authToken
     * @param event_id
     */
    public EventRequest(String authToken, String event_id) {
        AuthToken = authToken;
        this.event_id = event_id;
    }

    public String getAuthToken() {
        return AuthToken;
    }

    public void setAuthToken(String authToken) {
        AuthToken = authToken;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }
}
