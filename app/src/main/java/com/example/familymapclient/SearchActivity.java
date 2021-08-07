package com.example.familymapclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import Model.Event;
import Model.Person;
import cache.DataCache;

public class SearchActivity extends AppCompatActivity {
    private static final int PERSON_ITEM = 0;
    private static final int EVENT_ITEM = 1;
    private SearchView searchView;
    private PersonAndEventsViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        List<Person> people = new ArrayList<>();
        List<Event> events  = new ArrayList<>();


        adapter = new PersonAndEventsViewAdapter(people, events);
        recyclerView.setAdapter(adapter);

        searchView = findViewById(R.id.SearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Person> filteredPeople = new ArrayList<>();
                List<Event> filteredEvents = new ArrayList<>();

                DataCache.getInstance().getFilteredModels(filteredPeople, filteredEvents, newText);

                adapter = new PersonAndEventsViewAdapter(filteredPeople, filteredEvents);
                recyclerView.setAdapter(adapter);
                return false;
            }
        });

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

    private class PersonAndEventsViewAdapter extends RecyclerView.Adapter<PersonAndEventsViewHolder> {
        private List<Person> people;
        private List<Event> events;

        PersonAndEventsViewAdapter(List<Person> people, List<Event> events){
            this.people = new ArrayList<>(people);
            this.events = new ArrayList<>(events);
        }

        @Override
        public int getItemViewType(int position) {
            return position < people.size() ? PERSON_ITEM : EVENT_ITEM;
        }
        @NonNull
        @Override
        public PersonAndEventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if(viewType == PERSON_ITEM){
                view = getLayoutInflater().inflate(R.layout.person_item, parent, false);
            }
            else{
                view = getLayoutInflater().inflate(R.layout.event_item, parent, false);
            }
            return new PersonAndEventsViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull PersonAndEventsViewHolder holder, int position) {
            if(position < people.size()){
                holder.bind(people.get(position));
            }
            else{
                holder.bind(events.get(position - people.size()));
            }
        }

        @Override
        public int getItemCount() {
            return people.size() + events.size();
        }

    }

    private class PersonAndEventsViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView personName;
        private TextView eventDescription;
        private TextView personEvent;
        private ImageView personGenderIcon;
        private ImageView eventMarker;
        private IconSetter icons;

        private final int viewType;
        private Event event;
        private Person person;

        PersonAndEventsViewHolder(View view, int viewType){
            super(view);
            this.viewType = viewType;
            icons = new IconSetter();

            itemView.setOnClickListener(this);

            if(viewType == PERSON_ITEM){
                personName = itemView.findViewById(R.id.PersonName);
                personGenderIcon = itemView.findViewById(R.id.PersonGenderIcon);
            }
            else{
                eventMarker = itemView.findViewById(R.id.eventMarker);
                eventDescription = itemView.findViewById(R.id.EventDescription);
                personEvent = itemView.findViewById(R.id.PersonEvent);
            }
        }

        private void bind(Event event){
            this.event = event;
            String personID = event.getPersonID();
            Person person = DataCache.getInstance().getPersonById(personID);

            icons.setMarkers(event, eventMarker);

            eventDescription.setText(event.getEventType().toUpperCase() + ": " + event.getCity() + ", " + event.getCountry()
                                    + "(" + event.getYear() + ")");

            personEvent.setText(person.getFirstName() + " " + person.getLastName());
        }

        private void bind(Person person){
            this.person = person;

            personName.setText(person.getFirstName() + " " + person.getLastName());

            icons.setGenderIcons(person, personGenderIcon);

        }

        @Override
        public void onClick(View v) {
            if(viewType == PERSON_ITEM){
                DataCache.getInstance().setCurrentPersonID(person.getPersonID());
                Intent in = new Intent(SearchActivity.this, PersonActivity.class);
                startActivity(in);
            }
            if(viewType == EVENT_ITEM){
                DataCache.getInstance().setCurrentEvent(event);
                Intent in = new Intent(SearchActivity.this, EventActivity.class);
                startActivity(in);
            }
        }
    }
}