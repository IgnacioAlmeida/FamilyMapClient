package com.example.familymapclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {
    Switch liftStorySwitch, familyTreeSwitch, spouseSwitch, fatherSideSwitch, motherSideSwitch, maleSwitch, femaleSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        liftStorySwitch = findViewById(R.id.lifeStorySwitch);
        liftStorySwitch.setChecked(true);

        familyTreeSwitch = findViewById(R.id.familyTreeSwitch);
        familyTreeSwitch.setChecked(true);

        spouseSwitch = findViewById(R.id.spouseSwitch);
        spouseSwitch.setChecked(true);

        fatherSideSwitch = findViewById(R.id.fatherSideSwitch);
        fatherSideSwitch.setChecked(true);

        motherSideSwitch = findViewById(R.id.motherSideSwitch);
        motherSideSwitch.setChecked(true);

        maleSwitch = findViewById(R.id.maleSwitch);
        maleSwitch.setChecked(true);

        femaleSwitch = findViewById(R.id.femaleSwitch);
        femaleSwitch.setChecked(true);
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