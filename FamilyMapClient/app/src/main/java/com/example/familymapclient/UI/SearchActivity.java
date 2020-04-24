package com.example.familymapclient.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.familymapclient.DataCache;
import com.example.familymapclient.R;
import com.example.shared.model.Event;
import com.example.shared.model.Person;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchActivity extends AppCompatActivity {

    private static final int PERSON_ITEM_VIEW_TYPE = 0;
    private static final int EVENT_ITEM_VIEW_TYPE = 1;
    private Context context;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
     super.onCreate(saveInstanceState);

        setContentView(R.layout.activity_search);
        context = this;

        RecyclerView recyclerView = findViewById(R.id.RecycleListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        List<Person> personList = new ArrayList<>(DataCache.getInstance().getInitialPeopleMap().values());
        List<Event> eventList = new ArrayList<>(DataCache.getInstance().getFilteredEvent().values());

        final ItemViewAdapter itemViewAdapter = new ItemViewAdapter(personList,eventList);
        recyclerView.setAdapter(itemViewAdapter);

        SearchView familyMapSearchView = (SearchView) findViewById(R.id.search_bar);
        familyMapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                itemViewAdapter.notifyDataSetChanged();
                itemViewAdapter.queryTextFilter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }



    private class ItemViewAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        private final List<Person> personList;
        private final List<Event> eventList;


        ArrayList<Person> displayPersonList = new ArrayList<>();
        ArrayList<Event> displayEventList = new ArrayList<>();

        private ItemViewAdapter(List<Person> personList, List<Event> eventList) {
            this.personList = personList;
            this.eventList = eventList;
        }

        @Override
        public int getItemViewType(int position) {
            return position < displayPersonList.size() ? PERSON_ITEM_VIEW_TYPE : EVENT_ITEM_VIEW_TYPE;
        }


        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if(viewType == PERSON_ITEM_VIEW_TYPE){
                view = getLayoutInflater().inflate(R.layout.item_person, parent, false);
            }else {
                view = getLayoutInflater().inflate(R.layout.item_event, parent, false);
            }

            return new ItemViewHolder(view,viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
                if(position < displayPersonList.size()){
                    holder.bind(displayPersonList.get(position));
                }else {
                    holder.bind(displayEventList.get(position- displayPersonList.size()));
                }
        }



        @Override
        public int getItemCount() {
            return displayPersonList.size() + displayEventList.size();
        }

        public void queryTextFilter(String query){
            displayPersonList = new ArrayList<>();
            displayEventList = new ArrayList<>();
            query = query.toLowerCase();
            for(Person person: personList){
                if(person.getLastName().toLowerCase().contains(query) || person.getFirstName().toLowerCase().contains(query)){
                    displayPersonList.add(person);
                }
            }

            for(Event event: eventList){
                String eventDescriptionString = event.getEventType() + ":" + event.getCity() + "," + event.getCountry() + "(" +
                        event.getYear() + ")";
                DataCache dataCache = DataCache.getInstance();
                Person eventOwner = dataCache.getInitialPeopleMap().get(event.getPersonID());
                String fullName = eventOwner.getFirstName() + " " + eventOwner.getLastName();
                if(fullName.toLowerCase().contains(query) || eventDescriptionString.toLowerCase().contains(query)){
                    displayEventList.add(event);
                }
            }
        }
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final ImageView imageView;
        private final TextView upperDescription;
        private final TextView lowerDescription;

        private final int viewType;
        private Person person;
        private Event event;

        public ItemViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            itemView.setOnClickListener(this);

            if(viewType == PERSON_ITEM_VIEW_TYPE){
                imageView = itemView.findViewById(R.id.personIcon);
                upperDescription = itemView.findViewById(R.id.personListName);
                lowerDescription = null;
            } else {
                imageView = null;
                upperDescription = itemView.findViewById(R.id.eventListEventDescription);
                lowerDescription = itemView.findViewById(R.id.eventListPersonName);
            }
        }

        private void bind(Person person) {
            this.person = person;
            if (person.getGender().equals("m")) {
                imageView.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_male)
                        .colorRes(R.color.male_icon).sizeDp(40));
            } else {
                imageView.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_female)
                        .colorRes(R.color.female_icon).sizeDp(40));
            }
            String fullName = person.getFirstName() + " " + person.getLastName();
            upperDescription.setText(fullName);
        }

        private void bind(Event event){
            this.event = event;
            String eventDescriptionString = event.getEventType() + ":" + event.getCity() + "," + event.getCountry() + "(" +
                    event.getYear() + ")";
            upperDescription.setText(eventDescriptionString);
            DataCache dataCache = DataCache.getInstance();
            Person eventOwner = dataCache.getInitialPeopleMap().get(event.getPersonID());
            String fullName = eventOwner.getFirstName() + " " + eventOwner.getLastName();
            lowerDescription.setText(fullName);

        }


        @Override
        public void onClick(View v) {
            if(viewType == PERSON_ITEM_VIEW_TYPE){
                DataCache.getInstance().setCurrentPerson(person);
                Intent intent = new Intent(context, PersonActivity.class);
                startActivity(intent);
            }else {
                DataCache.getInstance().setCurrentEvent(event);
                DataCache.getInstance().setCurrentPerson(DataCache.getInstance().getInitialPeopleMap().get(event.getPersonID()));
                Intent intent = new Intent(context,EventActivity.class);
                startActivity(intent);
            }

        }
    }
}





