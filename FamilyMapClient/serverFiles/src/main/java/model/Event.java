package model;


import java.util.Objects;

/**
 * a model to store events
 */
public class Event {

    String eventID;
    String associatedUsername;
    String personID;
    Float latitude;
    Float longitude;
    String country;
    String city;
    String eventType;
    Integer year;

    public Event() {
    }

    /**
     * set the parameters to from the constructors
     *
     * @param eventID
     * @param AssociatedUsername
     * @param person_id
     * @param latitude
     * @param longitude
     * @param country
     * @param city
     * @param eventType
     * @param year
     */
    public Event(String eventID, String AssociatedUsername, String person_id,
                 float latitude, float longitude, String country,
                 String city, String eventType, Integer year) {
        this.eventID = eventID;
        this.associatedUsername = AssociatedUsername;
        this.personID = person_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public boolean hasNull() {
        if (
                eventID == null ||
                        associatedUsername == null ||
                        personID == null ||
                        country == null ||
                        city == null ||
                        eventType == null ||
                        year == null

        )
            return true;
        return false;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return Float.compare(event.latitude, latitude) == 0 &&
                Float.compare(event.longitude, longitude) == 0 &&
                Objects.equals(eventID, event.eventID) &&
                Objects.equals(associatedUsername, event.associatedUsername) &&
                Objects.equals(personID, event.personID) &&
                Objects.equals(country, event.country) &&
                Objects.equals(city, event.city) &&
                Objects.equals(eventType, event.eventType) &&
                Objects.equals(year, event.year);
    }


    public String getEventID() {
        return eventID;
    }

    public void setEventID(String event_it) {
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

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
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


}
