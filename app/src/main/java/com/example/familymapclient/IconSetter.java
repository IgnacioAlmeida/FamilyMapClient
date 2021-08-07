package com.example.familymapclient;

import android.widget.ImageView;

import Model.Event;
import Model.Person;

public class IconSetter {

    IconSetter(){
    }
    public void setMarkers (Event currentEvent, ImageView groupIcon){
        if(currentEvent.getEventType().toUpperCase().equals("BIRTH")){
            groupIcon.setImageResource(R.drawable.marker_pink);
        }
        if(currentEvent.getEventType().toUpperCase().equals("MARRIAGE")){
            groupIcon.setImageResource(R.drawable.marker_green);
        }
        if(currentEvent.getEventType().toUpperCase().equals("DEATH")){
            groupIcon.setImageResource(R.drawable.marker_cyen);
        }
        if(currentEvent.getEventType().toUpperCase().equals("COMPLETED ASTEROIDS")){//TODO
            groupIcon.setImageResource(R.drawable.marker_magenta);
        }
        if(currentEvent.getEventType().toUpperCase().equals("GRADUATED FROM BYU")){
            groupIcon.setImageResource(R.drawable.marker_blue);
        }
        if(currentEvent.getEventType().toUpperCase().equals("DID A BACKFLIP")){
            groupIcon.setImageResource(R.drawable.marker_orange);
        }
        if(currentEvent.getEventType().toUpperCase().equals("LEARNED JAVA")){
            groupIcon.setImageResource(R.drawable.marker_yellow);
        }
        if(currentEvent.getEventType().toUpperCase().equals("CAUGHT A FROG")){
            groupIcon.setImageResource(R.drawable.marker_azure);
        }
        if(currentEvent.getEventType().toUpperCase().equals("ATE BRAZILIAN BARBECUE")){
            groupIcon.setImageResource(R.drawable.marker_red);
        }
        if(currentEvent.getEventType().toUpperCase().equals("LEARNED TO SURF")){
            groupIcon.setImageResource(R.drawable.marker_violet);
        }
    }
    
    public void setGenderIcons(Person person, ImageView personGenderIcon){
            if (person.getGender().equals("m")) {
                personGenderIcon.setImageResource(R.drawable.ic_male);
            }
            if (person.getGender().equals("f")) {
                personGenderIcon.setImageResource(R.drawable.ic_female);
            }
    }
}
