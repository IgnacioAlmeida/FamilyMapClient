package com.example.familymapclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import Model.Event;
import Model.Person;
import cache.DataCache;

public class PersonActivity extends AppCompatActivity {
    TextView firstNamePA, lastNamePA, genderPA;
    Person person;
    IconSetter markers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        markers = new IconSetter();
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

        Map<String, Person> closeRelatives = DataCache.getInstance().getCloseRelatives(personID);
        List<Event> personEvents = DataCache.getInstance().getPersonEvents(personID);
        ExpandableListView expandableListView = findViewById(R.id.expandableListView);
        expandableListView.setAdapter(new ExpandableListAdapter(personEvents, closeRelatives));
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private List<Event> personEvents;
        private Map<String, Person> closeRelatives;
        private static final int LIFE_EVENTS = 0;
        private static final int FAMILY = 1;




        ExpandableListAdapter(List<Event> personEvents, Map<String, Person> closeRelatives){
            this.personEvents = personEvents;
            this.closeRelatives = closeRelatives;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition){
                case LIFE_EVENTS:
                    return personEvents.size();
                case FAMILY:
                    return closeRelatives.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position");

            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition){
                case LIFE_EVENTS:
                    return getString(R.string.life_events_group);
                case FAMILY:
                    return getString(R.string.family);
                default:
                    throw new IllegalArgumentException("Unrecognized group position");
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case LIFE_EVENTS:
                    return personEvents.get(childPosition);
                case FAMILY:
                    return closeRelatives.get(childPosition);
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
                case FAMILY:
                    titleView.setText(R.string.family);
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
                    initializeEventsGroup(itemView,childPosition);
                    break;
                case FAMILY:
                    itemView = getLayoutInflater().inflate(R.layout.family_and_events_group, parent, false);
                    initializeFamilyGroup(itemView,childPosition);
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

        private void initializeEventsGroup(View EventsView, final int childPoistion) {
            TextView groupMain = EventsView.findViewById(R.id.PersonName);
            TextView groupSecondary = EventsView.findViewById(R.id.GroupSecondary);
            ImageView groupIcon = EventsView.findViewById(R.id.PersonGenderIcon);
            Event currentEvent = personEvents.get(childPoistion);
            groupMain.setText(currentEvent.getEventType().toUpperCase() + ": " + currentEvent.getCity() + ", "
                    + currentEvent.getCountry() + " (" + currentEvent.getYear() + ")");

            groupSecondary.setText(person.getFirstName() + " " + person.getLastName());

            markers.setMarkers(currentEvent, groupIcon);

            EventsView.setOnClickListener(v -> {
                DataCache.getInstance().setCurrentEvent(currentEvent);
                DataCache.getInstance().setInEventActivity(true);
                Intent in = new Intent(PersonActivity.this, EventActivity.class);
                startActivity(in);
            });
        }

        private void initializeFamilyGroup(View familyView, int childPosition){
            TextView groupMain = familyView.findViewById(R.id.PersonName);
            TextView groupSecondary = familyView.findViewById(R.id.GroupSecondary);
            ImageView groupIcon = familyView.findViewById(R.id.PersonGenderIcon);
            String relationship = null;
            String relationships[] = closeRelatives.keySet().toArray(new String[0]);

            relationship = relationships[childPosition];

            Person currentPerson = closeRelatives.get(relationship);

            String firstName = currentPerson.getFirstName();
            String lastName = currentPerson.getLastName();

            markers.setGenderIcons(currentPerson, groupIcon);


            groupMain.setText(firstName + " " + lastName);

            groupSecondary.setText(relationship);

            familyView.setOnClickListener(v -> {
                DataCache.getInstance().setCurrentPersonID(currentPerson.getPersonID());
                Intent in = new Intent(PersonActivity.this, PersonActivity.class);
                startActivity(in);
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