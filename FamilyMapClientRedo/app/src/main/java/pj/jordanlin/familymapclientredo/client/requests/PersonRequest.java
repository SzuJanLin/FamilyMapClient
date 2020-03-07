package client.requests;

/**
 * a request to get a person
 */
public class PersonRequest {

    private String AuthToken;
    private String personId;

    public PersonRequest() {
    }

    /**
     * non-default constructor
     * @param authToken
     * @param personId
     */
    public PersonRequest(String authToken, String personId) {
        AuthToken = authToken;
        this.personId = personId;
    }

    public String getAuthToken() {
        return AuthToken;
    }

    public void setAuthToken(String authToken) {
        AuthToken = authToken;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
}
