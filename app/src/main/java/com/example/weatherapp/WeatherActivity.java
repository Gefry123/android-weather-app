package com.example.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherActivity extends AppCompatActivity {
    private final String TAG = "WeatherActivity";
    String CITY;
    String API = "59f089250c5fc563637f3af51b77123d";
    String choice;
    TextView addressTxt,  statusTxt, tempTxt, temp_minTxt, temp_maxTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt;
    ImageButton settingsBttn;
    ConstraintLayout relaLay;

    private Toolbar toolbar;
    private String temperature_unit;

    private String temp;
    private String tempMin;
    private String tempMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Intent intent = getIntent();
        //grabbing the values from main activity so we can use them for the API call after
        CITY = intent.getStringExtra("City");
        choice = intent.getStringExtra("choose");
        Log.d("is it working the intent ", CITY);
        //Log.d("is it working the intent ", choice);


        addressTxt = findViewById(R.id.address);
        statusTxt = findViewById(R.id.status);
        settingsBttn = findViewById(R.id.weather_settings_bttn);
        tempTxt = findViewById(R.id.temp);
        temp_minTxt = findViewById(R.id.temp_min);
        temp_maxTxt = findViewById(R.id.temp_max);
        relaLay = findViewById(R.id.weather_lay);
        // if we decide to add more details
//        sunriseTxt = findViewById(R.id.sunrise);
//        sunsetTxt = findViewById(R.id.sunset);
//        windTxt = findViewById(R.id.wind);
//        pressureTxt = findViewById(R.id.pressure);
//        humidityTxt = findViewById(R.id.humidity);

        new weatherTask().execute();



        //SETTINGS TOOLBAR
        toolbar = findViewById(R.id.weather_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Load the preferences from the XML resource
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //Get the stored temperature preference
        temperature_unit = sharedPreferences.getString("temperature", "");
        //Listener for settings button
        settingsBttn.setOnClickListener(v -> {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        });



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
    protected void onResume() {
        super.onResume();
        // Load the preferences from the XML resource
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //Get the stored temperature preference
        String newTemperature_unit = sharedPreferences.getString("temperature", "");
        //change preferences dynamically
        if (!temperature_unit.equals(newTemperature_unit)) {
            temperature_unit = newTemperature_unit;
            tempTxt.setText(getTemperatureUnit(temp));
            temp_minTxt.setText("Min Temp: " + getTemperatureUnit(tempMin));
            temp_maxTxt.setText("Max Temp: " + getTemperatureUnit(tempMax));
        }

    }
    //METHOD TO SET TO CHANGE TEMPERATURE UNIT BASE ON PREFERENCE SELECTED
    private String getTemperatureUnit(String temperature){
        Log.d(TAG, "Inside getTemperatureUnit");

        if(temperature_unit.equalsIgnoreCase("fahrenheit")) {
            double fah = Double.parseDouble(temperature);
            fah = (fah * 1.8) + 32;
            return ((int) Math.round(fah)) + " °F";
        }
        else{
            double cel = Double.parseDouble(temperature);
            return ((int) Math.round(cel)) + " °C";
        }
    }




    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + API);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {


            try {
                JSONObject jsonObj = new JSONObject(result);
                Log.d("data from API", String.valueOf(jsonObj));
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject sys = jsonObj.getJSONObject("sys");
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                temp = main.getString("temp");
                tempMin = main.getString("temp_min");
                tempMax = main.getString("temp_max");

                String pressure = main.getString("pressure");
                String humidity = main.getString("humidity");

                Long sunrise = sys.getLong("sunrise");
                Long sunset = sys.getLong("sunset");
                String windSpeed = wind.getString("speed");
                String weatherDescription = weather.getString("description");

                String address = jsonObj.getString("name") + ", " + sys.getString("country");


                //change background
                if(weatherDescription.toUpperCase().equals("CLEAR SKY")){
                    relaLay.setBackground(getResources().getDrawable(R.drawable.clearsky));

                }else if(weatherDescription.toUpperCase().equals("RAIN")){
                    relaLay.setBackground(getResources().getDrawable(R.drawable.rain));

//                    relaLay.setBackgroundResource(R.drawable.rain);
                }else if(weatherDescription.toUpperCase().equals("BROKEN CLOUDS") || weatherDescription.toUpperCase().equals("SCATTERED CLOUDS")) {
//                    relaLay.setBackgroundResource(R.drawable.cloudy);
                    Log.d("background if check", weatherDescription);
                    relaLay.setBackground(getResources().getDrawable(R.drawable.cloudy));

                }



                /* Populating extracted data into our views */

                    addressTxt.setText(address);
                    statusTxt.setText(weatherDescription.toUpperCase());
                    tempTxt.setText(getTemperatureUnit(temp));
                    temp_minTxt.setText("Min Temp: " + getTemperatureUnit(tempMin));
                    temp_maxTxt.setText("Max Temp: " + getTemperatureUnit(tempMax));
//                sunriseTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
//                sunsetTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
//                windTxt.setText(windSpeed);
//                pressureTxt.setText(pressure);
//                humidityTxt.setText(humidity);

//                resultView.setText(windSpeed);



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}