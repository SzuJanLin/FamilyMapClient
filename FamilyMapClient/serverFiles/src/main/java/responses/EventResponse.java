package responses;

import java.util.Objects;

/**
 * a result to send back
 */
public class EventResponse {
    String eventID;
    String associatedUsername;
    String personID;
    Float latitude;
    Float longitude;
    String country;
    String city;
    String eventType;
    Integer year;
    String message;
    boolean success;

    public EventResponse() {
    }

    public EventResponse(String event_id, String descendant, String personID, Float latitude, Float longitude, String country, String city, String eventType, Integer year, String message, boolean success) {
        this.eventID = event_id;
        this.associatedUsername = descendant;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
        this.message = message;
        this.success = success;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventResponse)) return false;
        EventResponse that = (EventResponse) o;
        return Float.compare(that.latitude, latitude) == 0 &&
                Float.compare(that.longitude, longitude) == 0 &&
                success == that.success &&
                Objects.equals(eventID, that.eventID) &&
                Objects.equals(associatedUsername, that.associatedUsername) &&
                Objects.equals(personID, that.personID) &&
                Objects.equals(country, that.country) &&
                Objects.equals(city, that.city) &&
                Objects.equals(eventType, that.eventType) &&
                Objects.equals(year, that.year) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventID, associatedUsername, personID, latitude, longitude, country, city, eventType, year, message, success);
    }

    public String getEvent_id() {
        return eventID;
    }

    public void setEvent_id(String event_it) {
        this.eventID = event_it;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String person) {
        this.personID = person;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public float getLongitutde() {
        return longitude;
    }

    public void setLongitutde(float longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
