package pj.jordanlin.familymapclientredo.client.responses;

/**
 * Register Result for the service function
 */
public class RegisterResponse
{
    private String authToken;
    private String userName;
    private String personId;
    private String message;
    public RegisterResponse() {
    }

    /**
     * non-default constructor
     * @param authToken
     * @param userName
     * @param personId
     * @param  message
     */
    public RegisterResponse(String authToken, String userName, String personId, String message) {
        this.authToken = authToken;
        this.userName = userName;
        this.personId = personId;
        this.message = message;
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

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
