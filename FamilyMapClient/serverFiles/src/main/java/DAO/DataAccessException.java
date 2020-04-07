package DAO;

/**A special class for especially Data access exceptions*/
public class DataAccessException extends Exception {
    DataAccessException(String message) {
        super(message);
    }

    DataAccessException() {
        super();
    }
}
