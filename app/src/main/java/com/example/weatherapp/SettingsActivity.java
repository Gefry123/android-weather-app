package com.example.weatherapp;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceFragmentCompat;

import android.util.Log;
import android.os.Bundle;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity {

    private String TAG = "SettingsActivity";
    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Inside onCreate Method");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

                //SETTINGS TOOLBAR
                toolbar = findViewById(R.id.settings_toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == android.R.id.home) {
                    // Handle the click event of the back button
                    onBackPressed();
                    return true;
                }
                return super.onOptionsItemSelected(item);
            }

            public static class SettingsFragment extends PreferenceFragmentCompat {
                private String TAG = "SettingsFragment";

                @Override
                public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
                    Log.d(TAG, "Inside onCreatePreferences method");
                    setPreferencesFromResource(R.xml.preferences, rootKey);
                }
            }


    }
