package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import Responses.EventResponse;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginFragment();
    }

    public void loginFragment(){
        fragmentManager = getSupportFragmentManager();
        fragment = fragmentManager.findFragmentById(R.id.frameLayout);

        if(fragment == null) {
            fragment = new LoginFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.frameLayout, fragment)
                    .commit();
        }
    }
}

