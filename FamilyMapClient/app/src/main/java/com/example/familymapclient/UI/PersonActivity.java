package com.example.familymapclient.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.familymapclient.DataCache;
import com.example.familymapclient.R;
import com.example.shared.model.Event;
import com.example.shared.model.Person;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;


import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class PersonActivity extends AppCompatActivity {

    private ExpandableListView eventExpandableListView;

    private static final int EVENT_GROUP_POSITION = 0;
    private static final int PERSON_GROUP_POSITION = 1;

    private String firstName;
    private String lastName;
    private Person currentPerson;
    private Context context;

    @Override
    protected void onCreate(Bundle saveInstanceState) {


        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_person);
        context = this;
        Iconify.with(new FontAwesomeModule());

        DataCache dataCache = DataCache.getInstance();
        currentPerson = dataCache.getCurrentPerson();
        firstName = currentPerson.getFirstName();
        lastName = currentPerson.getLastName();


        TextView firstNameText = findViewById(R.id.personFirstName);
        TextView lastNameText = findViewById(R.id.personLastName);
        TextView genderText = findViewById(R.id.personGender);
        firstNameText.setText(firstName);
        lastNameText.setText(lastName);
        if (currentPerson.getGender().equals("m")) {
            genderText.setText("Male");
        } else {
            genderText.setText("Female");
        }

        List<Event> eventList = dataCache.getAPersonSortedEvents(currentPerson);
        Map<String, Event> filterEvents = dataCache.getFilteredEvent();
        if (!filterEvents.containsKey(eventList.get(0).getEventID())) {
            eventList.clear();
        }


        final List<Person> personList = dataCache.getAPersonRelatives(currentPerson);
        eventExpandableListView = findViewById(R.id.ExpandableListView);
        eventExpandableListView.setAdapter(new ExpandableListEventAdapter(eventList, personList));

    }

    private class ExpandableListEventAdapter extends BaseExpandableListAdapter {


        private final List<Event> eventList;
        private final List<Person> personList;

        private ExpandableListEventAdapter(List<Event> eventList, List<Person> personList) {
            this.eventList = eventList;
            this.personList = personList;
        }


        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    return eventList.size();
                case PERSON_GROUP_POSITION:
                    return personList.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position" + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    return "EVENTS";
                case PERSON_GROUP_POSITION:
                    return "PERSONS";
                default:
                    throw new IllegalArgumentException("Unrecognized group position" + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    return eventList.get(childPosition);
                case PERSON_GROUP_POSITION:
                    return personList.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position" + groupPosition);
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
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }
            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    titleView.setText("EVENTS");
                    break;
                case PERSON_GROUP_POSITION:
                    titleView.setText("PERSONS");
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.item_event, parent, false);
                    initializeEventView(itemView, childPosition);
                    LinearLayout eventLayout = itemView.findViewById(R.id.eventItemLinearLayout);

                    eventLayout.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            DataCache.getInstance().setCurrentEvent(eventList.get(childPosition));
                            Intent intent = new Intent(context, EventActivity.class);
                            startActivity(intent);
                        }
                    });
                    break;
                case PERSON_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.item_person, parent, false);
                    initializePersonView(itemView, childPosition);
                    LinearLayout personLayout = itemView.findViewById(R.id.personItemLinearLayout);
                    personLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DataCache.getInstance().setCurrentPerson(personList.get(childPosition));
                            Intent intent = new Intent(context, PersonActivity.class);
                            startActivity(intent);
                        }
                    });
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }


            return itemView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        private void initializeEventView(View eventItemView, final int childPosition) {
            TextView eventDescription = eventItemView.findViewById(R.id.eventListEventDescription);
            Event childEvent = eventList.get(childPosition);
            String eventDescriptionString = childEvent.getEventType() + ":" + childEvent.getCity() + "," + childEvent.getCountry() + "(" +
                    childEvent.getYear() + ")";
            eventDescription.setText(eventDescriptionString);
            TextView eventOwner = eventItemView.findViewById(R.id.eventListPersonName);
            String fullName = firstName + " " + lastName;
            eventOwner.setText(fullName);
        }

        private void initializePersonView(View personItemView, final int childPosition) {
            String fullName = personList.get(childPosition).getFirstName() + " " + personList.get(childPosition).getLastName();

            ImageView genderImageView = personItemView.findViewById(R.id.personIcon);
            TextView personDescription = personItemView.findViewById(R.id.personListName);
            personDescription.setText(fullName);
            TextView personRelationShip = personItemView.findViewById(R.id.personListRelationship);
            personRelationShip.setText(relationShipIdentifier(personList.get(childPosition)));

            if (personList.get(childPosition).getGender().equals("m")) {
                genderImageView.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_male)
                        .colorRes(R.color.male_icon).sizeDp(40));
            } else {
                genderImageView.setImageDrawable(new IconDrawable(context, FontAwesomeIcons.fa_female)
                        .colorRes(R.color.female_icon).sizeDp(40));
            }

        }

        private String relationShipIdentifier(Person currentCalculatePerson) {

            if (currentPerson.getMotherID() != null && currentPerson.getMotherID().equals(currentCalculatePerson.getPersonID())) {
                return "Mother";
            } else if (currentPerson.getFatherID() != null && currentPerson.getFatherID().equals(currentCalculatePerson.getPersonID())) {
                return "Father";
            } else if (currentPerson.getSpouseID() != null && currentPerson.getSpouseID().equals(currentCalculatePerson.getPersonID())) {
                return "Spouse";
            } else if (currentPerson.getPersonID() != null && (currentPerson.getPersonID().equals(currentCalculatePerson.getMotherID()) ||
                    currentPerson.getPersonID().equals(currentCalculatePerson.getFatherID()))) {
                return "Child";
            } else {
                return "RELATIONSHIP_UNDEFINED";
            }
        }


    }


}
