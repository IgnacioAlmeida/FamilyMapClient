package cache;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import Model.Event;
import Model.Person;

public class DataCache {

    private static DataCache instance;

    private String authToken;

    private Person[] people;

    private Event[] events;

    private boolean status;

    private boolean loginStatus;

    private String currentPersonID;

    private Event currentEvent;

    private boolean inEventActivity;

    private boolean lifeStorySwitchBool;

    private boolean familyTreeSwitchBool;

    private boolean spouseSwitchBool;

    private boolean fatherSideSwitch;

    private boolean motherSideSwitch;

    private boolean maleSwitch;

    private boolean femaleSwitch;

    private int settingsVisitCounter;

    private LatLng drawnEventLocation;

    private Person drawnPersonInfo;

    private String drawnEventID;

    private String loggedInPersonID;

    public List<Person> motherSide = new ArrayList<>();

    private List<Event> motherSideEvents;

    private  List<Person> fatherSide = new ArrayList<>();

    private List<Event> fatherSideEvents;

    private List<Person> getMotherAncestors(Person mother){
        String grandmaID = mother.getMotherID();
        String grandpaID = mother.getFatherID();
        Person grandma = getPersonById(grandmaID);
        Person grandpa = getPersonById(grandpaID);

        if(grandma != null){
            this.motherSide.add(grandma);
            getMotherAncestors(grandma);
        }
       if(grandpa != null){
            this.motherSide.add(grandpa);
           getMotherAncestors(grandpa);
        }

        return motherSide;
    }


    public void setMotherSideEvents(){
        Person loggedInPerson = getPersonById(loggedInPersonID);
        Person loggedInPersonMother = getPersonById(loggedInPerson.getMotherID());

        List<Person> motherSide = getMotherAncestors(loggedInPersonMother);
        List<Event> motherSideEvents = new ArrayList<>();
        motherSide.add(loggedInPerson);
        motherSide.add(loggedInPersonMother);

        for(Person p : motherSide){
            List<Event> currentPersonEvents = getPersonEvents(p.getPersonID());
            for(Event e : currentPersonEvents){
                motherSideEvents.add(e);
            }
        }
        this.motherSideEvents = motherSideEvents;
    }

    public List<Event> getMotherSideEvents(){
        return motherSideEvents;
    }

    private List<Person> getFatherAncestors(Person father){
        String grandmaID = father.getMotherID();
        String grandpaID = father.getFatherID();
        Person grandma = getPersonById(grandmaID);
        Person grandpa = getPersonById(grandpaID);

        if(grandma != null){
            this.fatherSide.add(grandma);
            getFatherAncestors(grandma);
        }
        if(grandpa != null){
            this.fatherSide.add(grandpa);
            getFatherAncestors(grandpa);
        }

        return fatherSide;
    }
    public void setFatherSideEvents(){
        Person loggedInPerson = getPersonById(loggedInPersonID);
        Person loggedInPersonFather = getPersonById(loggedInPerson.getFatherID());

        List<Person> fatherSide = getFatherAncestors(loggedInPersonFather);
        List<Event> fatherSideEvents = new ArrayList<>();
        fatherSide.add(loggedInPerson);
        fatherSide.add(loggedInPersonFather);

        for(Person p : fatherSide){
            List<Event> currentPersonEvents = getPersonEvents(p.getPersonID());
            for(Event e : currentPersonEvents){
                fatherSideEvents.add(e);
            }
        }
        this.fatherSideEvents = fatherSideEvents;
    }
    public List<Event> getFatherSideEvents(){
        return fatherSideEvents;
    }





    public List<Event> getMaleEvents(){
        List<Event> personEvents = new LinkedList<>();

        for(int i = 0; i < events.length; i++){
            String personGender = DataCache.getInstance().getPersonById(events[i].getPersonID()).getGender();
            if(personGender.equals("m")){
                personEvents.add(events[i]);
            }
        }

        return personEvents;
    }

    public List<Event> getFemaleEvents(){
        List<Event> personEvents = new LinkedList<>();

        for(int i = 0; i < events.length; i++){
            String personGender = DataCache.getInstance().getPersonById(events[i].getPersonID()).getGender();
            if(personGender.equals("f")){
                personEvents.add(events[i]);
            }
        }

        return personEvents;
    }

    public String getLoggedInPersonID() {
        return loggedInPersonID;
    }

    public void setLoggedInPersonID(String loggedInPersonID) {
        this.loggedInPersonID = loggedInPersonID;
    }

    public String getDrawnEventID() {
        return drawnEventID;
    }

    public void setDrawnEventID(String drawnEventID) {
        this.drawnEventID = drawnEventID;
    }

    public LatLng getDrawnEventLocation() {
        return drawnEventLocation;
    }

    public void setDrawnEventLocation(LatLng drawnEventLocation) {
        this.drawnEventLocation = drawnEventLocation;
    }

    public Person getDrawnPersonInfo() {
        return drawnPersonInfo;
    }

    public void setDrawnPersonInfo(Person drawnPersonInfo) {
        this.drawnPersonInfo = drawnPersonInfo;
    }

    public int getSettingsVisitCounter() {
        return settingsVisitCounter;
    }

    public void setSettingsVisitCounter(int settingsVisitCounter) {
        this.settingsVisitCounter = settingsVisitCounter;
    }

    public void getFilteredModels(List<Person> filteredPeople, List<Event> filteredEvents, String input){
        for(Person p : people){
            if(p.getFirstName().toLowerCase().contains(input) || p.getLastName().contains(input)){
                filteredPeople.add(p);
            }
        }
        for(Event e : events){
            if(e.getCountry().toLowerCase().contains(input) || e.getCity().toLowerCase().contains(input) || e.getEventType().toLowerCase().contains(input)){
                filteredEvents.add(e);
            }
        }
    }

    public boolean isLoginStatus() {
        return loginStatus;
    }

    public boolean isLifeStorySwitchBool() {
        return lifeStorySwitchBool;
    }

    public void setLifeStorySwitchBool(boolean liftStorySwitchBool) {
        this.lifeStorySwitchBool = liftStorySwitchBool;
    }

    public boolean isFamilyTreeSwitchBool() {
        return familyTreeSwitchBool;
    }

    public void setFamilyTreeSwitchBool(boolean familyTreeSwitchBool) {
        this.familyTreeSwitchBool = familyTreeSwitchBool;
    }

    public boolean isSpouseSwitchBool() {
        return spouseSwitchBool;
    }

    public void setSpouseSwitchBool(boolean spouseSwitchBool) {
        this.spouseSwitchBool = spouseSwitchBool;
    }

    public boolean isFatherSideSwitch() {
        return fatherSideSwitch;
    }

    public void setFatherSideSwitch(boolean fatherSideSwitch) {
        this.fatherSideSwitch = fatherSideSwitch;
    }

    public boolean isMotherSideSwitch() {
        return motherSideSwitch;
    }

    public void setMotherSideSwitch(boolean motherSideSwitch) {
        this.motherSideSwitch = motherSideSwitch;
    }

    public boolean isMaleSwitch() {
        return maleSwitch;
    }

    public void setMaleSwitch(boolean maleSwitch) {
        this.maleSwitch = maleSwitch;
    }

    public boolean isFemaleSwitch() {
        return femaleSwitch;
    }

    public void setFemaleSwitch(boolean femaleSwitch) {
        this.femaleSwitch = femaleSwitch;
    }

    public boolean isInEventActivity() {
        return inEventActivity;
    }

    public void setInEventActivity(boolean inEventActivity) {
        this.inEventActivity = inEventActivity;
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    public Event getEventById(String eventID){
        for(int i = 0; i < events.length; i++){
            if(events[i].getEventID().equals(eventID)){
                return events[i];
            }
        }
        return null;
    }

    public Person getPersonById(String personID){
        for(int i = 0; i < people.length; i++){
            if(people[i].getPersonID().equals(personID)){
                return people[i];
            }
        }
        return null;
    }

    public Map<String, Person> getCloseRelatives(String personID){
        Map<String, Person> closeRelatives = new HashMap<>();
        Person currentPerson = null;
        Person child = null;
        for(int i = 0; i < people.length; i++){
            if(people[i].getPersonID().equals(personID)){
                currentPerson = people[i];
            }
            if(people[i].getFatherID() != null && people[i].getMotherID() != null) {
                if (people[i].getFatherID().equals(personID)) {
                    child = people[i];
                }
                if (people[i].getMotherID().equals(personID)) {
                    child = people[i];
                }
            }
        }

        String fatherID = currentPerson.getFatherID();
        String motherID = currentPerson.getMotherID();
        String spouseID = currentPerson.getSpouseID();

        Person father = null;
        Person mother = null;
        Person spouse = null;

        for(int i = 0; i < people.length; i++){
            if(people[i].getPersonID().equals(fatherID)){
                father = people[i];
            }
            if(people[i].getPersonID().equals(motherID)){
                mother = people[i];
            }
            if(people[i].getPersonID().equals(spouseID)){
                spouse = people[i];
            }
        }

        if(father != null) {
            closeRelatives.put("Father", father);
        }
        if(mother != null) {
            closeRelatives.put("Mother", mother);
        }
        if(child != null) {
            closeRelatives.put("Child", child);
        }
        if(spouse != null){
            closeRelatives.put("Spouse", spouse);
        }

        return closeRelatives;
    }

    public String getCurrentPersonID() {
        return currentPersonID;
    }

    public void setCurrentPersonID(String currentPersonID) {
        this.currentPersonID = currentPersonID;
    }

    public boolean getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

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

    public List<Event> getPersonEvents(String personID){
        List<Event> personEvents = new LinkedList<>();

        for(int i = 0; i < events.length; i++){
            if(events[i].getPersonID().equals(personID)){
                personEvents.add(events[i]);
            }
        }

        List<Event> chronologicalEvents = new LinkedList<>();

        Event temp = null;

        for(int i = 0; i < personEvents.size(); i++){
            if(temp == null){
                chronologicalEvents.add(personEvents.get(i));
                temp = personEvents.get(i);
            }
            else if(personEvents.get(i).getYear() < temp.getYear()){
                chronologicalEvents.add(personEvents.get(i));
                temp = personEvents.get(i);
            }
        }
        return personEvents;
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



//    public void setEventsByPersonId (){
//        List<Event> personEvents = new LinkedList();
//
//        for(int i = 0; i < events.length; i++){
//            if(events[i].getPersonID() == events[i+1].getPersonID()) {
//                personEvents.add(events[i]);
//                if(i + 2 == events.length){
//                    personEvents.add(events[i+1]);
//                    eventsByPersonId.put(events[i].getPersonID(), personEvents);
//                    personEvents.clear();
//                    break;
//                }
//                if(events[i+1].getPersonID() != events[i+2].getPersonID()) {
//                    eventsByPersonId.put(events[i].getPersonID(), personEvents);
//                    personEvents.clear();
//                }
//            }
//        }
//    }


    public Event getEvent (String eventID){
        for(int i = 0; i < events.length; i++){
            if(events[i].getEventID().equals(eventID)){
                return events[i];
            }
        }
        return null;
    }

    public Person getPerson (String personID){
        for(int i = 0; i < people.length; i++){
            if(people[i].getPersonID().equals(personID)){
                return people[i];
            }
        }
        return null;
    }

}
