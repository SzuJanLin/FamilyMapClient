package com.example.familymapclient.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.familymapclient.DataCache;
import com.example.shared.model.Event;
import com.example.shared.model.Person;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import com.example.familymapclient.R;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public class MapFragment extends Fragment implements OnMapReadyCallback {


    private GoogleMap googleMap;
    private Map<String, Marker> markerMap = new TreeMap<>();
    private Map<String, Event> allEvents;
    private Map<String, HashSet<Event>> eventTypeMap = null;
    private Event displayEvent;
    private TextView eventDescription;
    private TextView personDescription;
    private Context context = getActivity();
    private ArrayList<Polyline> polylines = new ArrayList<>();
    public static final String ARG_TITLE = "title";
    private ImageView genderImageView;
    private Map<String, Integer> mapTypeInteger = new TreeMap<>();

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());

        setHasOptionsMenu(this.getActivity().getClass().getSimpleName().equals("MainActivity"));
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {

        menuInflater.inflate(R.menu.main_menu, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.fileMenuSearch);

        searchMenuItem.setIcon(new IconDrawable(this.getActivity(),
                FontAwesomeIcons.fa_filter) .colorRes(R.color.white) .actionBarSize());
        MenuItem eventMenuItem = menu.findItem(R.id.fileMenuSetting);
        eventMenuItem.setIcon(new IconDrawable(this.getActivity(),FontAwesomeIcons.fa_gear).colorRes(R.color.white).actionBarSize());

        searchMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //goes to search
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                return false;
            }
        });

        eventMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //goes to setting
                Intent intent = new Intent(getActivity(),SettingActivity.class);
                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        super.onCreateView(layoutInflater, viewGroup, bundle);
        View view = layoutInflater.inflate(R.layout.fragment_map, viewGroup, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        try {
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DataCache dataCache = DataCache.getInstance();
        dataCache.calculateFilteredEvents();
        allEvents = dataCache.getInitialEventMap();
        sortEvents(allEvents);
        int loopInt = 0;
        for (String eventTypeString : eventTypeMap.keySet()) {
            mapTypeInteger.put(eventTypeString,loopInt);
            if (loopInt == 9) {
                loopInt = 1;
            } else {
                loopInt++;
            }
        }




        genderImageView = view.findViewById(R.id.genderIcon);
        eventDescription = view.findViewById(R.id.eventDescription);
        personDescription = view.findViewById(R.id.personDescription);
        LinearLayout infoWindow = view.findViewById(R.id.infoWindow);

        infoWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (displayEvent != null) {
                    DataCache dataCache = DataCache.getInstance();
                    dataCache.setCurrentPerson(dataCache.getInitialPeopleMap().get(displayEvent.getPersonID()));
                    Intent intent = new Intent(getActivity(), PersonActivity.class);
                    startActivity(intent);
                }
            }
        });


        return view;
    }

    @Override
    public void onResume() {

        super.onResume();
        if(googleMap != null) {
            googleMap.clear();
            onMapReady(googleMap);
        }
    }

    @Override
    public void onMapReady(GoogleMap gMap) {

        googleMap = gMap;


        DataCache dataCache = DataCache.getInstance();
        dataCache.calculateFilteredEvents();
        allEvents = dataCache.getFilteredEvent();
        sortEvents(allEvents);

        markerMap.clear();
        for (String eventTypeString : eventTypeMap.keySet()) {
            generateMarkers(mapTypeInteger.get(eventTypeString), eventTypeMap.get(eventTypeString));
        }

        if (dataCache.getCurrentEvent() != null) {
            clicksMarker(markerMap.get(dataCache.getCurrentEvent().getEventID()));
        }

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                clicksMarker(marker);
                return false;
            }
        });
    }

    private void clicksMarker(Marker marker){
        for (Polyline line : polylines) {
            line.remove();
        }
        polylines.clear();

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        displayEvent = allEvents.get(marker.getTitle());
        DataCache dataCache = DataCache.getInstance();
        Person currentPerson = dataCache.getInitialPeopleMap().get(displayEvent.getPersonID());
        dataCache.setCurrentPerson(currentPerson);

        if (currentPerson.getGender().equals("m")) {
            genderImageView.setImageDrawable(new IconDrawable(getActivity(), FontAwesomeIcons.fa_male)
                    .colorRes(R.color.male_icon).sizeDp(40));
        } else {
            genderImageView.setImageDrawable(new IconDrawable(getActivity(), FontAwesomeIcons.fa_female)
                    .colorRes(R.color.female_icon).sizeDp(40));
        }

        String personDescriptionString = currentPerson.getFirstName() + " " + currentPerson.getLastName();
        String eventDescriptionString = displayEvent.getEventType() + ":" + displayEvent.getCity() + "," + displayEvent.getCountry() + "(" +
                displayEvent.getYear() + ")";
        eventDescription.setText(eventDescriptionString);
        personDescription.setText(personDescriptionString);
        if(dataCache.isSpouseOn()) {
            drawSpouseLine();
        }
        if(dataCache.isFamilyTreeOn()) {
            drawFamilyTreeLines(dataCache.getInitialPeopleMap());
        }
        if(dataCache.isLifeStoryOn()) {
            drawLifeStoryLines(dataCache.getAPersonSortedEvents(currentPerson));
        }
    }

    private void sortEvents(Map<String, Event> initialMap) {
        eventTypeMap = new TreeMap<>();
        for (String eventId : initialMap.keySet()) {
            Event currentEvent = initialMap.get(eventId);
            if (currentEvent != null) {
                String eventType = currentEvent.getEventType();
                if (!eventTypeMap.containsKey(eventType.toUpperCase())) {
                    eventTypeMap.put(eventType.toUpperCase(), new HashSet<Event>());
                }
                //add the event into the eventTypeMap
                eventTypeMap.get(eventType.toUpperCase()).add(currentEvent);
            }
        }


    }

    private void generateMarkers(int currentColor, HashSet<Event> eventHashSet) {

        LatLng currentEventLocation = null;
        for (Event currentEvent : eventHashSet) {
            Marker addingMarker = null;
            try {
                currentEventLocation = new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude());
                switch (currentColor) {
                    case 1:
                        addingMarker = googleMap.addMarker(new MarkerOptions().position(currentEventLocation).icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).title(currentEvent.getEventID()));
                        break;
                    case 2:
                        addingMarker = googleMap.addMarker(new MarkerOptions().position(currentEventLocation).icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)).title(currentEvent.getEventID()));
                        break;
                    case 3:
                        addingMarker = googleMap.addMarker(new MarkerOptions().position(currentEventLocation).icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).title(currentEvent.getEventID()));
                        break;
                    case 4:
                        addingMarker = googleMap.addMarker(new MarkerOptions().position(currentEventLocation).icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(currentEvent.getEventID()));
                        break;
                    case 5:
                        addingMarker = googleMap.addMarker(new MarkerOptions().position(currentEventLocation).icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title(currentEvent.getEventID()));
                        break;
                    case 6:
                        addingMarker = googleMap.addMarker(new MarkerOptions().position(currentEventLocation).icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(currentEvent.getEventID()));
                        break;
                    case 7:
                        addingMarker = googleMap.addMarker(new MarkerOptions().position(currentEventLocation).icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)).title(currentEvent.getEventID()));
                        break;
                    case 8:
                        addingMarker = googleMap.addMarker(new MarkerOptions().position(currentEventLocation).icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(currentEvent.getEventID()));
                        break;
                    case 9:
                        addingMarker = googleMap.addMarker(new MarkerOptions().position(currentEventLocation).icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE)).title(currentEvent.getEventID()));
                        break;
                    default:
                        addingMarker = googleMap.addMarker(new MarkerOptions().position(currentEventLocation).icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)).title(currentEvent.getEventID()));
                }
                markerMap.put(currentEvent.getEventID(), addingMarker);
            } catch (NullPointerException e) {
                e.getMessage();
            }
        }
    }

    private void drawSpouseLine() {
        DataCache dataCache = DataCache.getInstance();

        String spouseID = dataCache.getCurrentPerson().getSpouseID();
        if (spouseID != null) {
            Event spouseEvent = dataCache.getAPersonSortedEvents(dataCache.getInitialPeopleMap().get(spouseID)).get(0);
            if(!markerMap.containsKey(displayEvent.getEventID()))
                return;
            if(!markerMap.containsKey(spouseEvent.getEventID()))
                return;
            Polyline polyline = googleMap.addPolyline(new PolylineOptions().add(markerMap.get(displayEvent.getEventID()).getPosition(),
                    markerMap.get(spouseEvent.getEventID()).getPosition()).width(15).color(Color.RED));
            polylines.add(polyline);
        }
    }

    private void drawFamilyTreeLines(Map<String, Person> ancestorPool) {
        DataCache dataCache = DataCache.getInstance();
        if (ancestorPool.get(displayEvent.getPersonID()).getMotherID() != null) {
            String parentID = ancestorPool.get(displayEvent.getPersonID()).getMotherID();
            String newParentEventID = dataCache.getAPersonSortedEvents(ancestorPool.get(parentID)).get(0).getEventID();
            familyLinesHelper(displayEvent.getEventID(), newParentEventID, ancestorPool, 25);
        }
        if (ancestorPool.get(displayEvent.getPersonID()).getFatherID() != null) {
            String parentID = ancestorPool.get(displayEvent.getPersonID()).getFatherID();
            String newParentEventID = dataCache.getAPersonSortedEvents(ancestorPool.get(parentID)).get(0).getEventID();
            familyLinesHelper(displayEvent.getEventID(), newParentEventID, ancestorPool, 25);
        }


    }

    private void familyLinesHelper(String currentEventID, String parentEventID, Map<String, Person> ancestorPool, float lineSize) {

        if(!markerMap.containsKey(currentEventID) || !markerMap.containsKey(parentEventID))
            return;
        LatLng currentLocation = markerMap.get(currentEventID).getPosition();
        LatLng parentLocation = markerMap.get(parentEventID).getPosition();

        Polyline polyline = googleMap.addPolyline(new PolylineOptions().add(currentLocation, parentLocation).width(lineSize).color(Color.GRAY));
        polylines.add(polyline);
        lineSize *= 0.6;
        DataCache dataCache = DataCache.getInstance();
        String newCurrentPersonID = dataCache.getInitialEventMap().get(parentEventID).getPersonID();

        if (ancestorPool.get(newCurrentPersonID).getMotherID() != null) {
            String parentID = ancestorPool.get(newCurrentPersonID).getMotherID();
            String newParentEventID = dataCache.getAPersonSortedEvents(ancestorPool.get(parentID)).get(0).getEventID();
            familyLinesHelper(parentEventID, newParentEventID, ancestorPool, lineSize);
        }
        if (ancestorPool.get(newCurrentPersonID).getFatherID() != null) {
            String parentID = ancestorPool.get(newCurrentPersonID).getFatherID();
            String newParentEventID = dataCache.getAPersonSortedEvents(ancestorPool.get(parentID)).get(0).getEventID();
            familyLinesHelper(parentEventID, newParentEventID, ancestorPool, lineSize);
        }

    }

    private void drawLifeStoryLines(ArrayList<Event> lifeEvents) {

        for (int i = 0; i < lifeEvents.size() - 1; i++) {
            Polyline polyline = googleMap.addPolyline(new PolylineOptions().add(markerMap.get(lifeEvents.get(i).getEventID()).getPosition(),
                    markerMap.get(lifeEvents.get(i + 1).getEventID()).getPosition()).width(13).color(Color.GREEN));
            polylines.add(polyline);
        }
    }

}



