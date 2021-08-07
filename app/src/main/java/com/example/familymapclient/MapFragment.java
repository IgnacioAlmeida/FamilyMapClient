package com.example.familymapclient;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import Model.Event;
import Model.Person;
import cache.DataCache;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap map;
    Event[] eventsArr = DataCache.getInstance().getEvents();
    private List<Polyline> linesDrawn;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(DataCache.getInstance().isInEventActivity() == false) {
            inflater.inflate(R.menu.main_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.searchItem:
                    Intent in = new Intent(getActivity(), SearchActivity.class);
                    startActivity(in);
                return true;
            case R.id.settingsItem:
                    Intent inSet = new Intent(getActivity(), SettingsActivity.class);
                    startActivity(inSet);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_map, container,false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        map.setOnMarkerClickListener(this);
        List<Event> events = Arrays.asList(eventsArr.clone());
        DataCache.getInstance().setLifeStorySwitchBool(true);
        DataCache.getInstance().setFamilyTreeSwitchBool(true);
        DataCache.getInstance().setSpouseSwitchBool(true);
        DataCache.getInstance().setFatherSideSwitch(true);
        DataCache.getInstance().setMotherSideSwitch(true);
        DataCache.getInstance().setMaleSwitch(true);
        DataCache.getInstance().setFemaleSwitch(true);

        setPins(events);
        linesDrawn = new LinkedList<>();
        LinearLayout bottomText = getView().findViewById(R.id.eventHolder);
        bottomText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               TextView personInfo = getView().findViewById(R.id.NameText);

                if (!personInfo.getText().toString().equals("Click on a marker to see event details")) {
                    Intent in = new Intent(getActivity(), PersonActivity.class);
                    startActivity(in);
                } else {
                    Toast.makeText(getActivity(), "Please, make sure that you first select a marker", Toast.LENGTH_LONG).show();
                }
            }
        });


        if (DataCache.getInstance().isInEventActivity()) {
            Event currentEvent = DataCache.getInstance().getCurrentEvent();
            LatLng currentEventLocation = new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLng(currentEventLocation));
            markerClicked(currentEvent.getEventID());
        }


    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String eventID = marker.getTag().toString();
        markerClicked(eventID);


        return false;
    }

    public void markerClicked(String eventID){
        if(eventID != null){
            DataCache.getInstance().setCurrentEvent(DataCache.getInstance().getEvent(eventID));
            Event event = DataCache.getInstance().getEvent(eventID);
            String personID = event.getPersonID();
            if(personID != null){
                Person person = DataCache.getInstance().getPerson(personID);
                ImageView image = (ImageView) getView().findViewById(R.id.genderDisplay);
                if(person.getGender().equals("m")){
                    image.setImageResource(R.drawable.ic_male);
                }
                if(person.getGender().equals("f")){
                    image.setImageResource(R.drawable.ic_female);
                }
                ((TextView) getView().findViewById(R.id.NameText)).setText(person.getFirstName() + " " + person.getLastName() + "\n"
                        + event.getEventType() + ": " + event.getCity()
                        + ", " + event.getCountry() + " (" + event.getYear() + ")");
                DataCache.getInstance().setCurrentPersonID(personID);

                if(linesDrawn.size() > 0) {
                    for (int i = 0; i < linesDrawn.size(); i++) {
                        linesDrawn.get(i).remove();
                    }
                }
                drawLines(eventID, personID);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(map != null) {
            if (DataCache.getInstance().getSettingsVisitCounter() > 0) {
                map.clear();
                List<Event> events = new ArrayList<>();
                Person loggedInPerson = DataCache.getInstance().getPersonById(DataCache.getInstance().getLoggedInPersonID());
                DataCache.getInstance().setMotherSideEvents();
                DataCache.getInstance().setFatherSideEvents();
                List<Event> spouseEvents = new ArrayList<>();
                if(loggedInPerson.getSpouseID() != null){
                    spouseEvents = DataCache.getInstance().getPersonEvents(loggedInPerson.getSpouseID());
                }

                if(loggedInPerson.getGender().equals("f")){
                    if(DataCache.getInstance().isFemaleSwitch()){
                        List<Event> personEvents = DataCache.getInstance().getPersonEvents(loggedInPerson.getPersonID());
                        for(Event e : personEvents){
                            events.add(e);
                        }
                        if(DataCache.getInstance().isMaleSwitch()){
                            if(loggedInPerson.getSpouseID() != null) {
                                for (Event e : spouseEvents) {
                                    events.add(e);
                                }
                            }
                        }
                    }
                }else{
                    if(DataCache.getInstance().isMaleSwitch()){
                        List<Event> personEvents = DataCache.getInstance().getPersonEvents(loggedInPerson.getPersonID());
                        for(Event e : personEvents){
                            events.add(e);
                        }
                        if(DataCache.getInstance().isFemaleSwitch()){
                            if(loggedInPerson.getSpouseID() != null) {
                                for (Event e : spouseEvents) {
                                    events.add(e);
                                }
                            }
                        }
                    }
                }

                if(DataCache.getInstance().isFatherSideSwitch()){
                    List<Event> fatherSideEvents = DataCache.getInstance().getFatherSideEvents();
                    for(Event e : fatherSideEvents){
                        if(!events.contains(e)){
                            String gender = DataCache.getInstance().getPersonById(e.getPersonID()).getGender();
                            if(gender.equals("m") && DataCache.getInstance().isMaleSwitch()){
                                events.add(e);
                            }
                            if(gender.equals("f") && DataCache.getInstance().isFemaleSwitch()){
                                events.add(e);
                            }
                        }
                    }
                }
                if(DataCache.getInstance().isMotherSideSwitch()){
                    List<Event> motherSideEvents = DataCache.getInstance().getMotherSideEvents();
                    for(Event e : motherSideEvents){
                        if(!events.contains(e)){
                            String gender = DataCache.getInstance().getPersonById(e.getPersonID()).getGender();
                            if(gender.equals("m") && DataCache.getInstance().isMaleSwitch()){
                                events.add(e);
                            }
                            if(gender.equals("f") && DataCache.getInstance().isFemaleSwitch()){
                                events.add(e);
                            }
                        }
                    }
                }

                setPins(events);
                markerClicked(DataCache.getInstance().getDrawnEventID());
                Person currentPerson = DataCache.getInstance().getPersonById(DataCache.getInstance().getCurrentPersonID());
                if(!(currentPerson.getGender().equals("m") && DataCache.getInstance().isMaleSwitch() || currentPerson.getGender().equals("f") && DataCache.getInstance().isFemaleSwitch())){
                    ((TextView) getView().findViewById(R.id.NameText)).setText(R.string.non_existent_marker);
                }
            }
        }
    }

    public void setPins(List<Event> events){
        for(int i = 0; i < events.size(); i++){
            LatLng eventLocation = new LatLng(events.get(i).getLatitude(), events.get(i).getLongitude());
            String eventType = events.get(i).getEventType();
            if(eventType.toUpperCase().equals("BIRTH")){
                map.addMarker(new MarkerOptions().position(eventLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))).setTag(events.get(i).getEventID());
            }
            if(eventType.toUpperCase().equals("MARRIAGE")){
                map.addMarker(new MarkerOptions().position(eventLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))).setTag(events.get(i).getEventID());
            }
            if(eventType.toUpperCase().equals("DEATH")){
                map.addMarker(new MarkerOptions().position(eventLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))).setTag(events.get(i).getEventID());
            }
            if(eventType.toUpperCase().equals("COMPLETED ASTEROIDS")){
                map.addMarker(new MarkerOptions().position(eventLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))).setTag(events.get(i).getEventID());
            }
            if(eventType.toUpperCase().equals("GRADUATED FROM BYU")){
                map.addMarker(new MarkerOptions().position(eventLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))).setTag(events.get(i).getEventID());
            }
            if(eventType.toUpperCase().equals("DID A BACKFLIP")){
                map.addMarker(new MarkerOptions().position(eventLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))).setTag(events.get(i).getEventID());
            }
            if(eventType.toUpperCase().equals("LEARNED JAVA")){
                map.addMarker(new MarkerOptions().position(eventLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))).setTag(events.get(i).getEventID());
            }
            if(eventType.toUpperCase().equals("CAUGHT A FROG")){
                map.addMarker(new MarkerOptions().position(eventLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).setTag(events.get(i).getEventID());
            }
            if(eventType.toUpperCase().equals("ATE BRAZILIAN BARBECUE")){
                map.addMarker(new MarkerOptions().position(eventLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))).setTag(events.get(i).getEventID());
            }
            if(eventType.toUpperCase().equals("LEARNED TO SURF")){
                map.addMarker(new MarkerOptions().position(eventLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))).setTag(events.get(i).getEventID());
            }

            if(DataCache.getInstance().getCurrentEvent() != null) {
                LatLng currentEventLocation = new LatLng(DataCache.getInstance().getCurrentEvent().getLatitude(), DataCache.getInstance().getCurrentEvent().getLongitude());
                map.animateCamera(CameraUpdateFactory.newLatLng(currentEventLocation));
            }
            else{
                map.animateCamera(CameraUpdateFactory.newLatLng(eventLocation));
            }
        }
    }

    public void drawLines(String eventID, String personID){
        Event event = DataCache.getInstance().getEventById(eventID);
        Person person = DataCache.getInstance().getPersonById(personID);
        LatLng eventLocation = new LatLng(event.getLatitude(), event.getLongitude());
        DataCache.getInstance().setDrawnEventLocation(eventLocation);
        DataCache.getInstance().setDrawnPersonInfo(person);
        DataCache.getInstance().setDrawnEventID(eventID);
        Person currentPerson = DataCache.getInstance().getPersonById(DataCache.getInstance().getCurrentPersonID());

        if(DataCache.getInstance().getSettingsVisitCounter() == 0 || DataCache.getInstance().isInEventActivity()) {
            drawSpouseLines(eventLocation, person);
            drawFamilyTreeLines(eventLocation, person, 16);
            drawLifeStoryLines(person);
        }
        else{
            if(DataCache.getInstance().isSpouseSwitchBool()){
                drawSpouseLines(eventLocation, person);
            }
            if(DataCache.getInstance().isFamilyTreeSwitchBool()) {
                if(currentPerson.getGender().equals("m") && DataCache.getInstance().isMaleSwitch() || currentPerson.getGender().equals("f") && DataCache.getInstance().isFemaleSwitch()) {
                    drawFamilyTreeLines(eventLocation, person, 16);
                }
            }
            if(DataCache.getInstance().isLifeStorySwitchBool()) {
                if(currentPerson.getGender().equals("m") && DataCache.getInstance().isMaleSwitch() || currentPerson.getGender().equals("f") && DataCache.getInstance().isFemaleSwitch()) {
                    drawLifeStoryLines(person);
                }
            }
        }
    }

    public void drawSpouseLines(LatLng eventLocation, Person person){
        if (person.getSpouseID() != null) {
            String spouseID = person.getSpouseID();
            Person spouse = DataCache.getInstance().getPersonById(spouseID);
            if((spouse.getGender().equals("m") && DataCache.getInstance().isMaleSwitch() || spouse.getGender().equals("f") && DataCache.getInstance().isFemaleSwitch())
                && (person.getGender().equals("m") && DataCache.getInstance().isMaleSwitch() || person.getGender().equals("f") && DataCache.getInstance().isFemaleSwitch())) {

                List<Event> spouseEvents = DataCache.getInstance().getPersonEvents(spouseID);

                LatLng spouseEventLocation = new LatLng(spouseEvents.get(0).getLatitude(), spouseEvents.get(0).getLongitude());
                Polyline spouseLine = map.addPolyline(new PolylineOptions()
                        .add(eventLocation, spouseEventLocation)
                        .width(10)
                        .color(Color.WHITE));

                linesDrawn.add(spouseLine);
            }
        }
    }

    public void drawFamilyTreeLines(LatLng eventLocation, Person person, int width){
        drawFatherSideLines(eventLocation, person, width);
    }

    private void drawFatherSideLines(LatLng eventLocation, Person person, int width){
        if(DataCache.getInstance().isFatherSideSwitch() && DataCache.getInstance().isMaleSwitch()) {
            if (person.getFatherID() != null) {
                Person father = DataCache.getInstance().getPersonById(person.getFatherID());
                List<Event> fatherEvents = DataCache.getInstance().getPersonEvents(father.getPersonID());


                LatLng fatherEventLocation = new LatLng(fatherEvents.get(0).getLatitude(), fatherEvents.get(0).getLongitude());
                Polyline fatherLine = map.addPolyline(new PolylineOptions()
                        .add(eventLocation, fatherEventLocation)
                        .width(width)
                        .color(Color.YELLOW));

                linesDrawn.add(fatherLine);

                if (width > 4) {
                    width -= 4;
                } else {
                    width = 4;
                }

                drawFatherSideLines(fatherEventLocation, father, width);
            }
        }
        if(DataCache.getInstance().isMotherSideSwitch() && DataCache.getInstance().isFemaleSwitch()) {
            if (person.getMotherID() != null) {
                Person mother = DataCache.getInstance().getPersonById(person.getMotherID());
                List<Event> motherEvents = DataCache.getInstance().getPersonEvents(mother.getPersonID());

                LatLng motherEventLocation = new LatLng(motherEvents.get(0).getLatitude(), motherEvents.get(0).getLongitude());
                Polyline motherLine = map.addPolyline(new PolylineOptions()
                        .add(eventLocation, motherEventLocation)
                        .width(width)
                        .color(Color.GREEN));

                linesDrawn.add(motherLine);

                if (width > 4) {
                    width -= 4;
                } else {
                    width = 4;
                }

                drawFatherSideLines(motherEventLocation, mother, width);
            }
        }
    }

    public void drawLifeStoryLines(Person person){
        Person currentPerson = DataCache.getInstance().getPersonById(DataCache.getInstance().getCurrentPersonID());
        if(currentPerson.getGender().equals("f") && DataCache.getInstance().isFemaleSwitch() || currentPerson.getGender().equals("m") && DataCache.getInstance().isMaleSwitch()) {
            List<Event> personEvents = new ArrayList<>();
            personEvents = DataCache.getInstance().getPersonEvents(person.getPersonID());

            if (personEvents.size() > 0) {
                for (int i = 1; i < personEvents.size(); i++) {
                    drawLifeStoryLinesHelper(personEvents.get(i - 1), personEvents.get(i));
                }
            }
        }

    }

    public void drawLifeStoryLinesHelper(Event previousEvent, Event event){
        LatLng previousEventLocation = new LatLng(previousEvent.getLatitude(),previousEvent.getLongitude());
        LatLng eventLocation = new LatLng(event.getLatitude(),event.getLongitude());

        Polyline storyLine = map.addPolyline(new PolylineOptions()
                .add(previousEventLocation, eventLocation)
                .width(10)
                .color(Color.RED));

        linesDrawn.add(storyLine);
    }

}