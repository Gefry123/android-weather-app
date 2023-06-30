package com.example.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import android.app.NotificationManager;

import com.google.android.material.navigation.NavigationView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView locationInput;
    private ImageButton addButton;
    private String CITY;
    private final String API = "59f089250c5fc563637f3af51b77123d";

    //DRAWER NAVIGATION VARIABLES
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    //TEMPERATURE UNIT
    private String temperature_unit;
    private ArrayList<CitiesItems> citiesItemsList;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    private String temp;

    TextView addressTxt,  statusTxt, tempTxt, temp_minTxt, temp_maxTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt;

    private String icon;

    private final String TAG ="MainActivity";
    private String address;
    private ItemTouchHelper itemTouchHelper;
    private int dragDirections = ItemTouchHelper.LEFT |ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
    private int swipeDirections = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addButton = findViewById(R.id.add_button);
        locationInput = findViewById(R.id.location_input);




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

        //FOR DRAG AND SWIPE CARDVIEWS
        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(dragDirections, swipeDirections) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAbsoluteAdapterPosition();
                Collections.swap(citiesItemsList,from, to);
                adapter.notifyItemMoved(from, to);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                citiesItemsList.remove(viewHolder.getAbsoluteAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAbsoluteAdapterPosition());
            }
        });

        //CODE FOR RECYCLERVIEW


        setupRecyclerView();
        new weatherTask().execute();

    }

    private void setUpCitiesItems(String cityName, String temperature, String image) {
        Log.d(TAG, "inside setUpCitiesItems");

        if (cityName != null && temperature != null && image != null) {
            for (CitiesItems newCity : citiesItemsList) {
                if (newCity.getCITY().equals(cityName)) {
                    newCity.setTemperature(temperature);
                    newCity.setIconUrl(image);
                    setupRecyclerView();
                    return;
                }
            }
            CitiesItems city = new CitiesItems(cityName.trim(), temperature.trim(), image.trim());
            System.out.println(city.getCITY() + " " + city.getTemperature() + " " + city.getIconUrl());
            citiesItemsList.add(city);
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "INSIDE onResume method");
        super.onResume();
        // Load the preferences from the XML resource
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //Get the stored temperature preference
        temperature_unit = sharedPreferences.getString("temperature", "");
        System.out.println(temperature_unit);


        new weatherTask().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //METHOD TO SET TO CHANGE TEMPERATURE UNIT BASE ON PREFERENCE SELECTED
    private String getTemperatureUnit(String temperature){

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
    //METHOD TO SETUP ADAPTER
    private void setupRecyclerView() {
        Log.d(TAG, "INSIDE setupRecyclerView method");
        if(citiesItemsList ==null){
            citiesItemsList = new ArrayList<>();
        }

        recyclerView = findViewById(R.id.mRecyclerView);

        //itemTouchHelper.attachToRecyclerView(recyclerView);

        adapter = new RecyclerViewAdapter(this, citiesItemsList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    protected  class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {
            Log.d(TAG, "INSIDE doInBackground method");
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + API);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "INSIDE onPostExecute method");

            try {
                JSONObject jsonObj = new JSONObject(result);
                Log.d("data from API", String.valueOf(jsonObj));
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject sys = jsonObj.getJSONObject("sys");
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);


                address = jsonObj.getString("name") + ", " + sys.getString("country");
                temp = main.getString("temp");
                icon = weather.getString("icon");

                System.out.println(address + " " + temp + " " + icon);


                //populating the ArrayList
                setUpCitiesItems(address, getTemperatureUnit(temp), icon);

                //ADDITIONAL INFO

                String pressure = main.getString("pressure");
                String humidity = main.getString("humidity");
                Long sunrise = sys.getLong("sunrise");
                Long sunset = sys.getLong("sunset");
                String windSpeed = wind.getString("speed");
                String weatherDescription = weather.getString("description");





            } catch (JSONException e) {
            e.printStackTrace();
            }

        }
    }


}
