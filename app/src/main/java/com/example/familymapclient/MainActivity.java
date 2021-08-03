package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import Responses.EventResponse;
import cache.DataCache;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginFragment();

//        findViewById(R.id.SignInButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if(DataCache.getInstance().getLoginStatus() == true){
////                    getSupportFragmentManager().beginTransaction().
////                              replace(R.id.frameLayout,new MapFragment()).commit();
////                }
//
//            }
//        });
    }

    public void loginFragment(){
        fragmentManager = getSupportFragmentManager();
        fragment = fragmentManager.findFragmentById(R.id.frameLayout);

        if(fragment == null) {
            fragment = new LoginFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frameLayout, fragment)
                    .commit();
        }
    }

    public void mapFragment(){
        fragmentManager = getSupportFragmentManager();
        fragment = fragmentManager.findFragmentById(R.id.frameLayout);

        if(fragment == null){
            fragment = new MapFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.frameLayout, fragment)
                    .commit();
        }
    }
}

