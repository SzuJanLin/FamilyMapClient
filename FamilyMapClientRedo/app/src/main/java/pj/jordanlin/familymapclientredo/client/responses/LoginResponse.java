package pj.jordanlin.familymapclientredo.client.responses;

/**
 * return the Result for login
 */
public class LoginResponse
{
private String authToken;
private String userName;
private String personID;
private String message;

    public LoginResponse() {
    }



    /**
     * non-default constructor
     * @param authToken
     * @param userName
     * @param personID
     */
    public LoginResponse(String authToken, String userName, String personID, String message) {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;

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
