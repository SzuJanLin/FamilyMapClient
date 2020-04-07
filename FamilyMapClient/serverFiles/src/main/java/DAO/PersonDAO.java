package DAO;


import model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * the class to handle persons
 */
public class PersonDAO {


    private final Connection conn;

    /**
     * initialize the database
     *
     * @param conn
     * @throws Exception
     */
    public PersonDAO(Connection conn) throws Exception {
        this.conn = conn;
    }


    /**
     * get a array of persons associated with the user
     */
    public ArrayList<Person> getPeople(String descendant) {
        String sql = "SELECT firstName, lastName, personId, AssociatedUsername,gender,father,mother,spouse FROM Person WHERE AssociatedUsername = ?";
        Person output = null;
        ArrayList<Person> people = null;

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, descendant);
            ResultSet rs = pstmt.executeQuery();

            //input the data into model
            while (rs.next()) {
                output = new Person(rs.getString("personId"), rs.getString("AssociatedUsername"),
                        rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"),
                        rs.getString("father"), rs.getString("mother"), rs.getString("spouse"));


                if (people == null)
                    people = new ArrayList<>();
                people.add(output);
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return people;
    }


    /**
     * pass in a person's id and the username to get  a specific person
     *
     * @param personId
     * @return Person
     */
    public Person getPerson(String personId) throws DataAccessException {
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Person WHERE PersonId = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("personId"), rs.getString("AssociatedUsername"),
                        rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"),
                        rs.getString("father"), rs.getString("mother"), rs.getString("spouse"));
                if (person.getFatherID() != null && person.getMotherID() != null && person.getSpouseID() != null) {
                    if (person.getFatherID().equals(""))
                        person.setFatherID(null);
                    if (person.getMotherID().equals(""))
                        person.setMotherID(null);
                    if (person.getSpouseID().equals(""))
                        person.setSpouseID(null);
                }
                return person;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while finding person");
        }

        return null;
    }

    /**
     * create a person's id and store it
     *
     * @param person
     */
    public void addPerson(Person person) throws DataAccessException {
        String sql = "INSERT INTO Person(personId,AssociatedUsername,firstName,lastName," +
                "gender,father,mother,spouse) VALUES(?,?,?,?,?,?,?,?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, person.getPersonID());
            pstmt.setString(2, person.getAssociatedUsername());
            pstmt.setString(3, person.getFirstName());
            pstmt.setString(4, person.getLastName());
            pstmt.setString(5, person.getGender());
            pstmt.setString(6, person.getFatherID());
            pstmt.setString(7, person.getMotherID());
            pstmt.setString(8, person.getSpouseID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }


    /**
     * pass in a personId to delete a specific person.
     *
     * @param descendant
     */
    public void delete(String descendant) throws DataAccessException {
        String sql = "DELETE FROM Person Where AssociatedUsername = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, descendant);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Delete fail");

        }
    }

    /**
     * Clear the Table
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Person";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Fail to delete");
        }
    }


}
