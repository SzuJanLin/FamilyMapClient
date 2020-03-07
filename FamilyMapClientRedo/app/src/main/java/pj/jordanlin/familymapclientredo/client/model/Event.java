package client.model;



/**
 * a model to store events
 */
public class Event {

    String eventID;
    String descendant;
    String personID;
    String latitude;
    String longitude;
    String country;
    String city;
    String eventType;
    Integer year;
public Event(){}

    /**
     * set the parameters to from the constructors
     * @param eventID
     * @param descendant
     * @param person_id
     * @param latitude
     * @param longitude
     * @param country
     * @param city
     * @param eventType
     * @param year
     */
    public Event(String eventID, String descendant, String person_id,
                 String latitude, String longitude, String country,
                 String city, String eventType, Integer year)
    {
        this.eventID = eventID;
        this.descendant = descendant;
        this.personID = person_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public boolean hasNull()
    {
        if(
                eventID == null ||
                        descendant == null ||
                        personID == null ||
                        latitude == null ||
                        longitude == null ||
                        country== null ||
                        city == null ||
                        eventType == null ||
                        year == null

                )
            return true;
        return false;
    }


    public String getEventID() {
        return eventID;
    }

    public void setEventID(String event_it) {
        this.eventID = event_it;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String person) {
        this.personID = person;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
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
