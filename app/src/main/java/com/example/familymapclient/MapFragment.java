package com.example.familymapclient;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                //Here on click we call the
                ((TextView) getView().findViewById(R.id.NameText)).setText(person.getFirstName() + " " + person.getLastName());
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


}