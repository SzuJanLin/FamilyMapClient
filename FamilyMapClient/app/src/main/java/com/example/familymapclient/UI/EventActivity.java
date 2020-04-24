package com.example.familymapclient.UI;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.familymapclient.DataCache;
import com.example.familymapclient.R;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.Map;

public class EventActivity extends AppCompatActivity {

    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Iconify.with(new FontAwesomeModule());

        FragmentManager fm = this.getSupportFragmentManager();
        mapFragment = (MapFragment) fm.findFragmentById(R.id.insideMapActivityFrameLayout);
        if(mapFragment == null){
            mapFragment = createMapFragment("EVENT");
        }
        fm.beginTransaction().add(R.id.insideMapActivityFrameLayout,mapFragment).commit();

    }

    private MapFragment createMapFragment(String title){
        MapFragment fragment = new MapFragment();

        Bundle args = new Bundle();
        args.putString(MapFragment.ARG_TITLE,title);
        fragment.setArguments(args);

        return fragment;
    }
}
