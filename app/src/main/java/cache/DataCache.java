package cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import Model.Event;
import Model.Person;

public class DataCache {

    private static DataCache instance;

    private String authToken;

    private Map<String, Person> peoplebyId = new HashMap<>();

    private final Map<String, List<Person>> childrenByParentId = new HashMap<>();

    private final Map<String, SortedSet<Event>> eventsByPersonId = new HashMap<>();

    private Person[] people;

    private Event[] events;

    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static DataCache getInstance(){
        if(instance == null){
            instance = new DataCache();
        }
        return instance;
    }

    private DataCache(){

    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    //Create method that accepts array and put everything into the maps with for loops
    public Person getSinglePerson(int index){
        return people[index];
    }

    public Person[] getPeople() {
        return people;
    }

    public void setPeople(Person[] people) {
        this.people = people;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }

    public void setPeopleById(Person[] person){
        Map<String, Person> peopleByID = null;
        for(int i = 0; i < person.length; i++){
            peopleByID.put(person[i].getPersonID(), person[i]);
        }
        this.peoplebyId = peopleByID;
    }


    //    private final Comparator<Event> eventComparator = (event1, event2) ->{
//        String event1Type = event1.getEventType().toLowerCase();
//        String event2Type = event2.getEventType().toLowerCase();
//
//        switch(event1Type){
//
//        }
//    };
    private final Set<String> eventTypes = new HashSet<>();

    //User
    private Person user;

    //Immediate Family
    private final Set<Person> immediateFamilyMales = new HashSet<>();
    private final Set<Person> immediateFamilyFemales = new HashSet<>();

    //Ancestors
    private final Set<Person> fatherSideMales = new HashSet<>();
    private final Set<Person> fatherSideFemales = new HashSet<>();
    private final Set<Person> motherSideMales = new HashSet<>();
    private final Set<Person> motherSideFemales = new HashSet<>();


}
