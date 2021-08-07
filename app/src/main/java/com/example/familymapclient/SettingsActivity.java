package com.example.familymapclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import cache.DataCache;

public class SettingsActivity extends AppCompatActivity {
    Switch lifeStorySwitch, familyTreeSwitch, spouseSwitch, fatherSideSwitch, motherSideSwitch, maleSwitch, femaleSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        lifeStorySwitch = findViewById(R.id.lifeStorySwitch);

        familyTreeSwitch = findViewById(R.id.familyTreeSwitch);

        spouseSwitch = findViewById(R.id.spouseSwitch);

        fatherSideSwitch = findViewById(R.id.fatherSideSwitch);

        motherSideSwitch = findViewById(R.id.motherSideSwitch);

        maleSwitch = findViewById(R.id.maleSwitch);

        femaleSwitch = findViewById(R.id.femaleSwitch);

        if(DataCache.getInstance().getSettingsVisitCounter() == 0){
            lifeStorySwitch.setChecked(true);

            familyTreeSwitch.setChecked(true);

            spouseSwitch.setChecked(true);

            fatherSideSwitch.setChecked(true);

            motherSideSwitch.setChecked(true);

            maleSwitch.setChecked(true);

            femaleSwitch.setChecked(true);
        }
        else{
            lifeStorySwitch.setChecked(DataCache.getInstance().isLifeStorySwitchBool());

            familyTreeSwitch.setChecked(DataCache.getInstance().isFamilyTreeSwitchBool());

            spouseSwitch.setChecked(DataCache.getInstance().isSpouseSwitchBool());

            fatherSideSwitch.setChecked(DataCache.getInstance().isFatherSideSwitch());

            motherSideSwitch.setChecked(DataCache.getInstance().isMotherSideSwitch());

            maleSwitch.setChecked(DataCache.getInstance().isMaleSwitch());

            femaleSwitch.setChecked(DataCache.getInstance().isFemaleSwitch());
        }


        lifeStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    DataCache.getInstance().setLifeStorySwitchBool(true);
                }
                else{
                    DataCache.getInstance().setLifeStorySwitchBool(false);

                }
            }
        });
        familyTreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    DataCache.getInstance().setFamilyTreeSwitchBool(true);
                }
                else{
                    DataCache.getInstance().setFamilyTreeSwitchBool(false);

                }
            }
        });
        spouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    DataCache.getInstance().setSpouseSwitchBool(true);
                }
                else{
                    DataCache.getInstance().setSpouseSwitchBool(false);

                }
            }
        });
        fatherSideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    DataCache.getInstance().setFatherSideSwitch(true);
                }
                else{
                    DataCache.getInstance().setFatherSideSwitch(false);

                }
            }
        });
        motherSideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    DataCache.getInstance().setMotherSideSwitch(true);
                }
                else{
                    DataCache.getInstance().setMotherSideSwitch(false);

                }
            }
        });
        maleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    DataCache.getInstance().setMaleSwitch(true);
                }
                else{
                    DataCache.getInstance().setMaleSwitch(false);

                }
            }
        });
        femaleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    DataCache.getInstance().setFemaleSwitch(true);
                }
                else{
                    DataCache.getInstance().setFemaleSwitch(false);
                }
            }
        });

        DataCache.getInstance().setSettingsVisitCounter(+1);

        LinearLayout logout = findViewById(R.id.Logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCache.getInstance().clear();
                DataCache.getInstance().setLogout(true);
                Intent in = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(in);
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
}