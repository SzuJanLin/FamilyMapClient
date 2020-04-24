package com.example.familymapclient;

import com.example.shared.model.Event;
import com.example.shared.model.Person;
import com.example.shared.responses.EventResponse;
import com.example.shared.responses.EventsResponse;
import com.example.shared.responses.PersonResponse;
import com.example.shared.responses.PersonsResponse;

import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class dataCacheTest {

    DataCache dataCache = DataCache.getInstance();

    @BeforeEach
    public void setup(){

    }

    @AfterEach
    public void tearDown(){

    }

    @Test
    public void initialPersonDataInsertPass(){
        Person p1 = new Person("me","123","123","123","m","dad","mom","spouse");
        Person p2 = new Person("dad","123","123","123","m",null,null,"mom");
        Person p3 = new Person("mom","123","123","123","f",null,null,"mom");
        Person p4 = new Person("spouse","123","123","123","f",null,null,"me");

        ArrayList<Person> personArrayList = new ArrayList<>();
        personArrayList.add(p1);
        personArrayList.add(p2);
        personArrayList.add(p3);
        personArrayList.add(p4);
        PersonsResponse personResponse = new PersonsResponse(personArrayList,true);
        dataCache.initialPersonsDataInsert(personResponse);

        Map<String, Person> PeopleMap = dataCache.getInitialPeopleMap();

        assertEquals(p1,PeopleMap.get(p1.getPersonID()));
        assertEquals(p2,PeopleMap.get(p2.getPersonID()));
        assertEquals(p3,PeopleMap.get(p3.getPersonID()));
        assertEquals(p4,PeopleMap.get(p4.getPersonID()));
    }

    @Test
    public void initialPersonDataInsertFail(){
        Person p1 = new Person("me","123","123","123","m","dad","mom","spouse");
        Person p2 = new Person("me","123","123","123","m",null,null,"mom");
        Person p3 = new Person("me","123","123","123","f",null,null,"mom");
        Person p4 = new Person("me","123","123","123","f",null,null,"me");

        ArrayList<Person> personArrayList = new ArrayList<>();
        personArrayList.add(p1);
        personArrayList.add(p2);
        personArrayList.add(p3);
        personArrayList.add(p4);
        PersonsResponse personResponse = new PersonsResponse(personArrayList,true);
        dataCache.initialPersonsDataInsert(personResponse);

        Map<String, Person> PeopleMap = dataCache.getInitialPeopleMap();
        //data got messed up
        assertEquals(p4,PeopleMap.get(p1.getPersonID()));
    }

    @Test
    public void initialEventsDataInsertPass(){
        Event e1 = new Event("first","123","me",123,234,"Taiwan","123","eat",2020);
        Event e2 = new Event("second","123","me",123,254,"T34wan","1423","feed",2019);
        Event e3 = new Event("hers","123","spouse",132,233,"eijd","qwe","drink",1223);

        ArrayList<Event> eventArrayList = new ArrayList<>();
        eventArrayList.add(e1);
        eventArrayList.add(e2);
        eventArrayList.add(e3);
        EventsResponse eventsResponse = new EventsResponse(eventArrayList,true);
        dataCache.initialEventsDataInsert(eventsResponse);
        Map<String, HashSet<Event>> userIDWithEvents = dataCache.getUserIDWithEvents();
         int size = userIDWithEvents.get(e1.getPersonID()).size();
         assertEquals(3,size);
    }

    @Test
    public void initialEventsDataInsertFail(){
        Event e1 = new Event("first","123","me",123,234,"Taiwan","123","eat",2020);
        Event e2 = new Event("second","123","spuse",123,254,"T34wan","1423","feed",2019);
        Event e3 = new Event("hers","123","spouse",132,233,"eijd","qwe","drink",1223);

        ArrayList<Event> eventArrayList = new ArrayList<>();
        eventArrayList.add(e1);
        eventArrayList.add(e2);
        eventArrayList.add(e3);
        EventsResponse eventsResponse = new EventsResponse(eventArrayList,true);
        dataCache.initialEventsDataInsert(eventsResponse);
        Map<String, HashSet<Event>> userIDWithEvents = dataCache.getUserIDWithEvents();
        int size = userIDWithEvents.get(e1.getPersonID()).size();
        assertNotEquals(2,size);
    }
}
