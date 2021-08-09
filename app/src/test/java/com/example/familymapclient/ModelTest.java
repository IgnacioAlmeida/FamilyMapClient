package com.example.familymapclient;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import Model.Event;
import Model.Person;
import Requests.LoginRequest;
import Responses.LoginResponse;
import cache.DataCache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModelTest {
    private ServerProxy proxy;
    private LoginResponse loginResponse;
    private String authToken;

    @BeforeEach
    public void setUp(){
        proxy = new ServerProxy();
        LoginRequest login = new LoginRequest();
        login.setUserName("sheila");
        login.setPassword("parker");
        loginResponse = new LoginResponse();
        loginResponse = proxy.login(login);
        authToken = loginResponse.getAuthtoken();
        DataCache.getInstance().setPeople(proxy.getPeople(authToken).getData());
        DataCache.getInstance().setLoggedInPersonID(loginResponse.getPersonID());
        Event[] events = proxy.getEvents(authToken).getEvents();
        DataCache.getInstance().setEvents(events);

    }

    @AfterEach
    public void tearDown(){
        DataCache.getInstance().clear();
    }

    @Test
    public void familyRelationsPass(){
        assertEquals(DataCache.getInstance().getPerson(DataCache.getInstance().getLoggedInPersonID()).getMotherID(),
                        "Betty_White");
        assertEquals(DataCache.getInstance().getPerson(DataCache.getInstance().getLoggedInPersonID()).getFatherID(),
                "Blaine_McGary");
        assertEquals(DataCache.getInstance().getPerson(DataCache.getInstance().getLoggedInPersonID()).getSpouseID(),
                "Davis_Hyer");
    }

    @Test
    public void familyRelationsFail(){
        assertNotEquals(DataCache.getInstance().getPerson(DataCache.getInstance().getLoggedInPersonID()).getMotherID(),
                "Bettte");
        assertNotEquals(DataCache.getInstance().getPerson(DataCache.getInstance().getLoggedInPersonID()).getFatherID(),
                "Blainary");
        assertNotEquals(DataCache.getInstance().getPerson(DataCache.getInstance().getLoggedInPersonID()).getSpouseID(),
                "Daviser");
    }

    @Test
    public void filterEventsPass(){
        String loggedInPersonID = DataCache.getInstance().getLoggedInPersonID();
        Person loggedInPerson = DataCache.getInstance().getPersonById(loggedInPersonID);
        DataCache.getInstance().setFatherSideSwitch(true);
        Person Father = DataCache.getInstance().getPersonById(loggedInPerson.getFatherID());
        assertNotNull(DataCache.getInstance().getPersonEvents(Father.getPersonID()));
    }

    @Test
    public void filterEventsFail(){
        String loggedInPersonID = DataCache.getInstance().getLoggedInPersonID();
        Person loggedInPerson = DataCache.getInstance().getPersonById(loggedInPersonID);
        DataCache.getInstance().setFatherSideSwitch(true);
        Person Father = DataCache.getInstance().getPersonById(loggedInPerson.getFatherID());
        Event[] fatherEvents = DataCache.getInstance().getPersonEvents(Father.getFatherID()).toArray(new Event[0]);
        assertFalse(fatherEvents[0] == null);
    }

    @Test
    public void sortEventsPass(){
        assertTrue(DataCache.getInstance().getPersonEvents(DataCache.getInstance().getLoggedInPersonID()).get(0).getEventType().toUpperCase().equals("BIRTH"));
        assertTrue(DataCache.getInstance().getPersonEvents(DataCache.getInstance().getLoggedInPersonID()).get(4).getEventType().toUpperCase().equals("DEATH"));
    }

    @Test
    public void sortEventsFail(){
        assertFalse(DataCache.getInstance().getPersonEvents(DataCache.getInstance().getLoggedInPersonID()).get(0).getEventType().toUpperCase().equals("MARRIAGE"));
    }

    @Test
    public void searchPass(){
        DataCache.getInstance().setInTesting(true);
        List<Person> people = Arrays.asList(DataCache.getInstance().getPeople());
        List<Event> events = Arrays.asList(DataCache.getInstance().getEvents());

        DataCache.getInstance().setFemaleSwitch(true);
        DataCache.getInstance().getFilteredModels(people, events, "betty");

        assertTrue(DataCache.getInstance().getdEvents().size() == 0);
        assertTrue(DataCache.getInstance().getdFilteredPeople().size() == 1);
        DataCache.getInstance().setInTesting(false);
    }

    @Test
    public void searchFail(){
        DataCache.getInstance().setInTesting(true);

        List<Person> people = Arrays.asList(DataCache.getInstance().getPeople());
        List<Event> events = Arrays.asList(DataCache.getInstance().getEvents());

        DataCache.getInstance().getFilteredModels(people, events, "testingnoresultswhensearching");

        assertTrue(DataCache.getInstance().getdEvents().size() == 0);
        assertTrue(DataCache.getInstance().getdFilteredPeople().size() == 0);
        DataCache.getInstance().setInTesting(false);

    }
}
