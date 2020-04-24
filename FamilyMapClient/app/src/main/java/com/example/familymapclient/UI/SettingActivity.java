package com.example.familymapclient.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.familymapclient.DataCache;
import com.example.familymapclient.R;


public class SettingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_setting);

        Switch lifeStorySwitch = findViewById(R.id.lifeStorySwitch);
        Switch familyTreeSwitch = findViewById(R.id.familyTreeSwitch);
        Switch spouseLineSwitch = findViewById(R.id.spouseSwitch);
        Switch fatherSwitch = findViewById(R.id.fatherSwitch);
        Switch motherSwitch = findViewById(R.id.motherSwitch);
        Switch maleSwitch = findViewById(R.id.maleSwitch);
        Switch femaleSwitch = findViewById(R.id.femaleSwitch);
        LinearLayout logout = findViewById(R.id.logout);

        final DataCache dataCache = DataCache.getInstance();

        lifeStorySwitch.setChecked(dataCache.isLifeStoryOn());
        lifeStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dataCache.setLifeStoryOn(true);
                } else {
                    dataCache.setLifeStoryOn(false);
                }
            }
        });

        familyTreeSwitch.setChecked(dataCache.isFamilyTreeOn());
        familyTreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dataCache.setFamilyTreeOn(true);
                } else {
                    dataCache.setFamilyTreeOn(false);
                }
            }
        });

        spouseLineSwitch.setChecked(dataCache.isSpouseOn());
        spouseLineSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dataCache.setSpouseOn(true);
                } else {
                    dataCache.setSpouseOn(false);
                }
            }
        });

        fatherSwitch.setChecked(dataCache.isFatherOn());
        fatherSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dataCache.setFatherOn(true);
                } else {
                    dataCache.setFatherOn(false);
                }
                dataCache.calculateFilteredEvents();
            }
        });

        motherSwitch.setChecked(dataCache.isMotherOn());
        motherSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dataCache.setMotherOn(true);
                } else {
                    dataCache.setMotherOn(false);
                }
                dataCache.calculateFilteredEvents();
            }
        });

        maleSwitch.setChecked(dataCache.isMaleOn());
        maleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dataCache.setMaleOn(true);
                } else {
                    dataCache.setMaleOn(false);
                }
                dataCache.calculateFilteredEvents();
            }
        });

        femaleSwitch.setChecked(dataCache.isFemaleOn());
        femaleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dataCache.setFemaleOn(true);
                } else {
                    dataCache.setFemaleOn(false);
                }
                dataCache.calculateFilteredEvents();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCache.getInstance().getInitialEventMap().clear();
                DataCache.getInstance().getInitialPeopleMap().clear();
                DataCache.getInstance().setUserId(null);
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

    }
}
