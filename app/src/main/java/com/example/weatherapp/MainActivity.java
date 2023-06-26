package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText locationInput;
    private Spinner forecastSpinner;
    private String selectedForecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationInput = findViewById(R.id.location_input);
        forecastSpinner = findViewById(R.id.forecast_spinner);

        forecastSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedForecast = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    public void showWeather(View view) {
        String location = locationInput.getText().toString();

        Intent intent;
        if ("Today".equals(selectedForecast)) {
            // get weather for today
        } else {
            // get weather for tomorrow
        }
        // give location an start weather activity
//        intent.putExtra("location", location);
//        startActivity(intent);
    }
}
