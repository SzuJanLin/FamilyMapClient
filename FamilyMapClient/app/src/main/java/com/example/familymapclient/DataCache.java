package com.example.familymapclient;


import com.example.shared.model.Event;
import com.example.shared.model.Person;
import com.example.shared.responses.EventsResponse;
import com.example.shared.responses.PersonsResponse;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DataCache {

    private String userId = "";
    private Person currentPerson = new Person();
    private Event currentEvent = null;


    private Map<String, Person> initialPeopleMap = new TreeMap<>();
    private Map<String, Event> initialEventMap = new TreeMap<>();
    private Map<String, HashSet<Event>> userIDWithEvents = new TreeMap<>();
    private Map<String, Event> filteredEvent = new TreeMap<>();

    private Map<String, Person> fatherMale = new TreeMap<>();
    private Map<String, Person> fatherFemale = new TreeMap<>();
    private Map<String, Person> motherMale = new TreeMap<>();
    private Map<String, Person> motherFemale = new TreeMap<>();


    private boolean lifeStoryOn = true;
    private boolean familyTreeOn = true;
    private boolean spouseOn = true;
    private boolean fatherOn = true;
    private boolean motherOn = true;
    private boolean maleOn = true;
    private boolean femaleOn = true;


    private static DataCache instance;
    public static DataCache getInstance(){
        if(instance == null){
            instance = new DataCache();
        }
        return instance;
    }


    public void initialPersonsDataInsert(PersonsResponse personsResponse){
        ArrayList<Person> personArrayList = personsResponse.getData();
        for(int i =0; i< personArrayList.size(); i++){
            Person currentPerson = personArrayList.get(i);
            initialPeopleMap.put(currentPerson.getPersonID(),currentPerson);
        }
    }

    public void initialEventsDataInsert(EventsResponse eventsResponse){
        ArrayList<Event> eventArrayList = eventsResponse.getData();
        for(int i=0; i<eventArrayList.size(); i++){
            Event currentEvent = eventArrayList.get(i);
            initialEventMap.put(currentEvent.getEventID(),currentEvent);
            if(!userIDWithEvents.containsKey(currentEvent.getPersonID())){
                userIDWithEvents.put(currentEvent.getPersonID(),new HashSet<Event>());
            }
            userIDWithEvents.get(currentEvent.getPersonID()).add(currentEvent);
        }

    }

    /**Pass in person Id and we can start from here and sort out the family relations*/
    public void sortFamilyMember(String userId){
        Person user = initialPeopleMap.get(userId);
        if (user == null)
            return;
        getUserFatherParent(initialPeopleMap.get(user.getFatherID()));
        getUserMotherParent(initialPeopleMap.get(user.getMotherID()));
    }



    public void getUserFatherParent(Person currentPerson){
        if (currentPerson == null)
            return;

        if(currentPerson.getGender().equals("m")){
            fatherMale.put(currentPerson.getPersonID(),currentPerson);
        }else{
            fatherFemale.put(currentPerson.getPersonID(),currentPerson);
        }

        if(currentPerson.getFatherID() != null){
            getUserFatherParent(initialPeopleMap.get( currentPerson.getFatherID()));
        }
        if(currentPerson.getMotherID() != null){
            getUserFatherParent(initialPeopleMap.get(currentPerson.getMotherID()));
        }
    }

    public void getUserMotherParent(Person currentPerson){
        if (currentPerson == null)
            return;

        if(currentPerson.getGender().equals("m")){
            motherMale.put(currentPerson.getPersonID(),currentPerson);
        }else{
            motherFemale.put(currentPerson.getPersonID(),currentPerson);
        }

        if(currentPerson.getFatherID() != null){
            getUserMotherParent(initialPeopleMap.get( currentPerson.getFatherID()));
        }
        if(currentPerson.getMotherID() != null){
            getUserMotherParent(initialPeopleMap.get(currentPerson.getMotherID()));
        }
    }

    public ArrayList<Event> getAPersonSortedEvents(Person queryPerson){
        HashSet<Event> events = userIDWithEvents.get(queryPerson.getPersonID());
        Event[] eventArrayList = new Event[events.size()];
        int i = 0;
        for(Event currentEvent : events){
            eventArrayList[i] = currentEvent;
            i++;
        }
        Arrays.sort(eventArrayList, new Comparator<Event>() {

            @Override
            public int compare(Event o1, Event o2) {
                if(o1.getYear()-o2.getYear() == 0){
                    return o1.getEventType().charAt(0) - o2.getEventType().charAt(0);
                }

                return o1.getYear() - o2.getYear();
            }
        });

        return new ArrayList<>(Arrays.asList(eventArrayList).subList(0, events.size()));
    }

    /**Get a person's relatives in a array*/
    public ArrayList<Person> getAPersonRelatives(Person queryPerson){
        ArrayList<Person> relatives = new ArrayList<>();
        Person dad = null;
        Person mom = null;
        Person spouse = null;
        ArrayList<Person> child = new ArrayList<>();
        for(String key : initialPeopleMap.keySet()){
            Person currentPerson = initialPeopleMap.get(key);
            assert currentPerson != null;

            if(currentPerson.getPersonID()!=null&&currentPerson.getPersonID().equals(queryPerson.getFatherID())){
                dad = currentPerson;
            }
            else if(currentPerson.getPersonID()!=null&&currentPerson.getPersonID().equals(queryPerson.getMotherID())){
                mom = currentPerson;
            }
            else if(currentPerson.getPersonID()!=null&&currentPerson.getPersonID().equals(queryPerson.getSpouseID())){
                spouse = currentPerson;
            }
            else if(currentPerson.getFatherID()!=null&&currentPerson.getFatherID().equals(queryPerson.getPersonID())){
                child.add(currentPerson);
            }
            else if(currentPerson.getMotherID()!=null&&currentPerson.getMotherID().equals(queryPerson.getPersonID())){
                child.add(currentPerson);
            }
        }

        if (dad != null){
            relatives.add(dad);
        }
        if(mom != null){
            relatives.add(mom);
        }
        if(spouse != null){
            relatives.add(spouse);
        }
        relatives.addAll(child);

        return relatives;
    }

    public void calculateFilteredEvents(){
        Map<String, Event> filteredEvent = new HashMap<>();
        if (isFatherOn()){
            if(isMaleOn()){
                filteredEvent.putAll(getThisPersonEvent(fatherMale.values()));
            }
            if(isFemaleOn()){
                filteredEvent.putAll(getThisPersonEvent(fatherFemale.values()));
            }
        }
        if(isMotherOn()){
            if(isMaleOn()){
                filteredEvent.putAll(getThisPersonEvent(motherMale.values()));
            }
            if(isFemaleOn()){
                filteredEvent.putAll(getThisPersonEvent(motherFemale.values()));
            }
        }

        Person user = initialPeopleMap.get(userId);
        Person spouse = null;
        if(user.getSpouseID() != null) {
            spouse = initialPeopleMap.get(user.getSpouseID());
        }

        if(user.getGender().equals("m")){
            if(isMaleOn()){
                filteredEvent.putAll(getMap(userIDWithEvents.get(userId)));
            }
            if(isFemaleOn()){
                if (spouse != null)
                filteredEvent.putAll(getMap(userIDWithEvents.get(user.getSpouseID())));
            }
        }

        if(user.getGender().equals("f")){
            if(isFemaleOn()){
                filteredEvent.putAll(getMap(userIDWithEvents.get(userId)));
            }
            if(isMaleOn()){
                if(spouse != null)
                filteredEvent.putAll(getMap(userIDWithEvents.get(user.getSpouseID())));
            }
        }
        this.filteredEvent = filteredEvent;
    }

    public Map<String, HashSet<Event>> getUserIDWithEvents() {
        return userIDWithEvents;
    }

    public void setUserIDWithEvents(Map<String, HashSet<Event>> userIDWithEvents) {
        this.userIDWithEvents = userIDWithEvents;
    }

    private Map<String, Event> getMap(Collection<Event> events){
        Map<String, Event> maps = new HashMap<>();
        for(Event event : events){
            maps.put(event.getEventID(),event);
        }
        return maps;
    }

    private Map<String, Event> getThisPersonEvent(Collection<Person> persons){
        Map<String,Event> eventMap = new HashMap<>();
            for(Person person : persons) {
                for(Event event : new HashSet<Event>(userIDWithEvents.get(person.getPersonID())) ){
                    eventMap.put(event.getEventID(),event);
                }
            }

        return eventMap;
    }

    public Map<String, Event> getFilteredEvent() {
        return filteredEvent;
    }

    public Map<String, Person> getInitialPeopleMap() {
        return initialPeopleMap;
    }

    public Map<String, Event> getInitialEventMap() {
        return initialEventMap;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Person getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(Person currentPerson) {
        this.currentPerson = currentPerson;
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }


    public boolean isLifeStoryOn() {
        return lifeStoryOn;
    }

    public void setLifeStoryOn(boolean lifeStoryOn) {
        this.lifeStoryOn = lifeStoryOn;
    }

    public boolean isFamilyTreeOn() {
        return familyTreeOn;
    }

    public void setFamilyTreeOn(boolean familyTreeOn) {
        this.familyTreeOn = familyTreeOn;
    }

    public boolean isSpouseOn() {
        return spouseOn;
    }

    public void setSpouseOn(boolean spouseOn) {
        this.spouseOn = spouseOn;
    }

    public boolean isFatherOn() {
        return fatherOn;
    }

    public void setFatherOn(boolean fatherOn) {
        this.fatherOn = fatherOn;
    }

    public boolean isMotherOn() {
        return motherOn;
    }

    public void setMotherOn(boolean motherOn) {
        this.motherOn = motherOn;
    }

    public boolean isMaleOn() {
        return maleOn;
    }

    public void setMaleOn(boolean maleOn) {
        this.maleOn = maleOn;
    }

    public boolean isFemaleOn() {
        return femaleOn;
    }

    public void setFemaleOn(boolean femaleOn) {
        this.femaleOn = femaleOn;
    }



}
