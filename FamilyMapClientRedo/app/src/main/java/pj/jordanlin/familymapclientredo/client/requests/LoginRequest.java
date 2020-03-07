package client.requests;

/**
 * a class to login
 */
public class LoginRequest
{
    private String userName;
    private String password;



    public LoginRequest() {
    }

    /**
     * non default constructor
     * @param userName
     * @param password
     */
    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * if there is a Null inside the Request
     * @return
     */
  public boolean hasNull()
  {
      return true;
  }
}
