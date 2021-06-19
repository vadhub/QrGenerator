package com.vad.qrscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vad.qrscanner.fragments.LocationFragmentGeneration;
import com.vad.qrscanner.fragments.PhoneFragmentGeneration;
import com.vad.qrscanner.fragments.TextFragmentGeneration;

public class MainActivity extends AppCompatActivity{

    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        navigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_replacer, new PhoneFragmentGeneration());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selected = null;

            switch (item.getItemId()){
                case R.id.phone_nav:
                    selected = new PhoneFragmentGeneration();
                    break;

                case R.id.text_nav:
                    selected = new TextFragmentGeneration();
                    break;

                case R.id.coord_nav:
                    selected = new LocationFragmentGeneration();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_replacer, selected);
            return true;
        }
    };


}