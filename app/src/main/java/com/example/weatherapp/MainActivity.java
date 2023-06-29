package com.example.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;


//import android.app.NotificationManager;

import com.google.android.material.navigation.NavigationView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView locationInput;
   // private Spinner forecastSpinner;
    private String selectedForecast;
    private TextView resultView;
    ImageButton addButton;
    String CITY;
    String API = "59f089250c5fc563637f3af51b77123d";
    String myURL = "https://api.openweathermap.org/data/2.5/weather?q=";

    //DRAWER NAVIGATION VARIABLES
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    //TEMPERATURE UNIT
    private String temperature_unit;
    private DecimalFormat df = new DecimalFormat("#.##");


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addButton = findViewById(R.id.add_button);
        locationInput = findViewById(R.id.location_input);


//adding a notification

  /*        notifyBtn = findViewById(R.id.notify_btn);
//added a  notification button
        notifyBtn.setOnClickListener(new View.OnClickListener(){


  public void onClick(View v){

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,"MY Notification")
                builder.setContentTitle("From WeatherApp");
                builder.setContentText("Heading somewhere ? Check the weather forecast for your current location.");
                builder.setSmallIcon(R.drawable.ic_launcher_background);
                        builder.setAutoCancel(true);


                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
               // managerCompat.notify(1, builder.build());
        }

    }  */

        




        

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


        //CODE FOR PREFERENCES TO CHANGE CELSIUS TO FAHRENHEIT
        // Load the preferences from the XML resource
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //Get the stored temperature preference
        temperature_unit = sharedPreferences.getString("temperature", "");
        //Set the temperature based on selected preference

        //ADD BUTTON CODE FOR LISTENERS --- SENT VALUES TO WeatherActivity
        addButton.setOnClickListener(v -> {
            CITY = locationInput.getText().toString().trim();
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("City", CITY);
            startActivity(intent);
        });

        //DRAWER LAYOUT CODE
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.navitagionView);
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.menu_open,
                R.string.menu_close
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //SET NAVIGATION LISTENERS
        navigationView.setNavigationItemSelectedListener(item -> {
            // Handle navigation item selection here

            int itemId = item.getItemId();

            if (itemId == R.id.nav_notifications) {
                // Handle notifications item selection
            } else if (itemId == R.id.nav_settings) {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_share) {
                // Handle share item selection

            }

            drawerLayout.closeDrawers();
            return true;
        });


    }
    @Override
    protected void onResume() {
        super.onResume();
        // Load the preferences from the XML resource
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //Get the stored temperature preference
        temperature_unit = sharedPreferences.getString("temperature", "");

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


   /* public void showWeather(View view) {
        CITY = locationInput.getText().toString().trim();
        String myURL = "https://api.openweathermap.org/data/2.5/weather?q=" + CITY + API;
     // new weatherTask().execute();

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
