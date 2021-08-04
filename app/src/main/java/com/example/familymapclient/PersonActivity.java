package com.example.familymapclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import Model.Event;
import Model.Person;
import cache.DataCache;

public class PersonActivity extends AppCompatActivity {
    TextView firstNamePA, lastNamePA, genderPA;
    Person person;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        firstNamePA = findViewById(R.id.FirstNamePA);
        lastNamePA = findViewById(R.id.LastNamePA);
        genderPA = findViewById(R.id.GenderPA);

        String personID = DataCache.getInstance().getCurrentPersonID();
        person = DataCache.getInstance().getPerson(personID);
        firstNamePA.setText(person.getFirstName());
        lastNamePA.setText(person.getLastName());
        String gender = person.getGender();
        if(gender.equals("m")){
            genderPA.setText("Male");
        }
        else{
            genderPA.setText("Female");
        }

        List<Event> personEvents = DataCache.getInstance().getPersonEvents(personID);
        ExpandableListView expandableListView = findViewById(R.id.expandableListView);
        expandableListView.setAdapter(new ExpandableListAdapter(personEvents));
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        List<Event> personEvents;
        private static final int LIFE_EVENTS = 0;



        ExpandableListAdapter(List<Event> personEvents){
            this.personEvents = personEvents;
        }

        @Override
        public int getGroupCount() {
            return 1;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition){
                case LIFE_EVENTS:
                    return personEvents.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position");

            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition){
                case LIFE_EVENTS:
                    return getString(R.string.life_events_group);
                default:
                    throw new IllegalArgumentException("Unrecognized group position");
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case LIFE_EVENTS:
                    return personEvents.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position");
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.groups_titles, parent,false);
            }

            TextView titleView = convertView.findViewById(R.id.groupTitle);

            switch (groupPosition){
                case LIFE_EVENTS:
                    titleView.setText(R.string.life_events_group);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position");
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch (groupPosition){
                case LIFE_EVENTS:
                    itemView = getLayoutInflater().inflate(R.layout.family_and_events_group, parent, false);
                    initializeFamilyAndEventsGroup(itemView,childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position");
            }
            return itemView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        private void initializeFamilyAndEventsGroup(View familyAndEventsView, final int childPoistion) {
            TextView groupMain = familyAndEventsView.findViewById(R.id.GroupMain);
            TextView groupSecondary = familyAndEventsView.findViewById(R.id.GroupSecondary);
            ImageView groupIcon = familyAndEventsView.findViewById(R.id.GroupIcon);
            Event currentEvent = personEvents.get(childPoistion);
            groupMain.setText(currentEvent.getEventType().toUpperCase() + ": " + currentEvent.getCity() + ", "
                    + currentEvent.getCountry() + " (" + currentEvent.getYear() + ")");

            groupSecondary.setText(person.getFirstName() + " " + person.getLastName());

            if(currentEvent.getEventType().equals("Birth")){
                groupIcon.setImageResource(R.drawable.marker_pink);
            }
            if(currentEvent.getEventType().equals("Marriage")){
                groupIcon.setImageResource(R.drawable.marker_green);
            }
            if(currentEvent.getEventType().equals("Death")){
                groupIcon.setImageResource(R.drawable.marker_cyen);
            }
//            if (person.getGender().equals("m")) {
//                groupIcon.setImageResource(R.drawable.ic_male);
//            }
//            if (person.getGender().equals("f")) {
//                groupIcon.setImageResource(R.drawable.ic_female);
//            }

            familyAndEventsView.setOnClickListener(v -> {
                Toast.makeText(PersonActivity.this,"Make events activity happen",Toast.LENGTH_LONG).show();
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.person_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Intent intent = new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }
}