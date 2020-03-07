package pj.jordanlin.familymapclientredo.client.responses;

/**
 * a result to send back
 */
public class EventResponse
{
    String event_id;
    String descendant;
    String person_id;
    String latitude;
    String longitude;
    String country;
    String city;
    String evenType;
    Integer year;

    String message;
    public EventResponse(){}


    /**
     * non-default constructor
     * @param event_id
     * @param descendant
     * @param person_id
     * @param latitude
     * @param longitude
     * @param country
     * @param city
     * @param evenType
     * @param year
     * @param  message
     */
    public EventResponse(String event_id, String descendant, String person_id,
                         String latitude, String longitude, String country,
                         String city, String evenType, Integer year, String message)
    {
        this.event_id = event_id;
        this.descendant = descendant;
        this.person_id = person_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.evenType = evenType;
        this.year = year;
        this.message = message;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_it) {
        this.event_id = event_it;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person) {
        this.person_id = person;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitutde() {
        return longitude;
    }

    public void setLongitutde(String longitude) {
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

    public String getEvenType() {
        return evenType;
    }

    public void setEvenType(String evenType) {
        this.evenType = evenType;
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
}
