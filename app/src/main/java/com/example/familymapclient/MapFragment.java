package com.example.familymapclient;

import android.content.DialogInterface;
import android.content.Intent;
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

import Model.Event;
import Model.Person;
import cache.DataCache;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap map;
    Event[] events = DataCache.getInstance().getEvents();
    private ImageView searchIcon;
    private ImageView settingsIcon;


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
        inflater.inflate(R.menu.main_menu, menu);
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
        setPins(events);
        LinearLayout bottomText = getView().findViewById(R.id.eventHolder);
        bottomText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView personInfo = getView().findViewById(R.id.NameText);
                if(!personInfo.getText().toString().equals("Click on a marker to see event details")) {
                    Toast.makeText(getActivity(), "Person Activity is still being developed", Toast.LENGTH_LONG).show();
                    Intent in = new Intent(getActivity(), PersonActivity.class);
                    startActivity(in);
                }
                else{
                    Toast.makeText(getActivity(), "Please, make sure that you first select a marker", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //clear lines
        String eventID = marker.getTag().toString();

        if(eventID != null){
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
            }
        }

        return false;
    }

    public void setPins(Event[] events){
        for(int i = 0; i < events.length; i++){
            LatLng eventLocation = new LatLng(events[i].getLatitude(), events[i].getLongitude());
            String eventType = events[i].getEventType();
            if(eventType.equals("Birth")){
                map.addMarker(new MarkerOptions().position(eventLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))).setTag(events[i].getEventID());
            }
            if(eventType.equals("Marriage")){
                map.addMarker(new MarkerOptions().position(eventLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))).setTag(events[i].getEventID());
            }
            if(eventType.equals("Death")){
                map.addMarker(new MarkerOptions().position(eventLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))).setTag(events[i].getEventID());
            }
            map.animateCamera(CameraUpdateFactory.newLatLng(eventLocation));
        }
    }

    public void drawLines(Event event, Person person){

    }

}