package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private EditText locationInput;
    private Spinner forecastSpinner;
    private String selectedForecast;
    private TextView resultView;
    String CITY;
    String API = "59f089250c5fc563637f3af51b77123d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationInput = findViewById(R.id.location_input);
       // forecastSpinner = findViewById(R.id.forecast_spinner);
       // resultView = findViewById(R.id.result);

   /*   forecastSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedForecast = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/
    }
    public void showWeather(View view) {
        CITY = locationInput.getText().toString().trim();
        String myURL = "https://api.openweathermap.org/data/2.5/weather?q=" + CITY + API;

//        new weatherTask().execute();

        if ("Today".equals(selectedForecast)) {
            Intent intent = new Intent(this, WeatherActivity.class);
            String message = CITY;
            intent.putExtra("City", message);
            intent.putExtra("choose", "Today");
            startActivity(intent);
            // get weather for today
        } else {

            Intent intent = new Intent(this, WeatherActivity.class);
            String message = CITY;
            intent.putExtra("City", message);
            intent.putExtra("choose", "Tomorrow");
            startActivity(intent);
            // get weather for tomorrow
        }
        // give location an start weather activity
//        intent.putExtra("location", location);
//        startActivity(intent);
    }

//    class weatherTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            /* Showing the ProgressBar, Making the main design GONE */
//            findViewById(R.id.loader).setVisibility(View.VISIBLE);
//            findViewById(R.id.errorText).setVisibility(View.GONE);
//        }
//
//        protected String doInBackground(String... args) {
//            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + API);
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//
//            try {
//                JSONObject jsonObj = new JSONObject(result);
//                JSONObject main = jsonObj.getJSONObject("main");
//                JSONObject sys = jsonObj.getJSONObject("sys");
//                JSONObject wind = jsonObj.getJSONObject("wind");
//                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);
//
//                Long updatedAt = jsonObj.getLong("dt");
//                String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
//                String temp = main.getString("temp") + "°C";
//                String tempMin = "Min Temp: " + main.getString("temp_min") + "°C";
//                String tempMax = "Max Temp: " + main.getString("temp_max") + "°C";
//                String pressure = main.getString("pressure");
//                String humidity = main.getString("humidity");
//
//                Long sunrise = sys.getLong("sunrise");
//                Long sunset = sys.getLong("sunset");
//                String windSpeed = wind.getString("speed");
//                String weatherDescription = weather.getString("description");
//
//                String address = jsonObj.getString("name") + ", " + sys.getString("country");
//
//
//                /* Populating extracted data into our views */
//
//
//                resultView.setText(windSpeed);
//                /* Views populated, Hiding the loader, Showing the main design */
//                findViewById(R.id.loader).setVisibility(View.GONE);
//
//
//            } catch (JSONException e) {
//                findViewById(R.id.loader).setVisibility(View.GONE);
//                findViewById(R.id.errorText).setVisibility(View.VISIBLE);
//            }
//
//        }
//    }

}
