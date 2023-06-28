package com.example.weatherapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherActivity extends AppCompatActivity {
    String CITY;
    String API = "add the key here";
    String choice;
    TextView addressTxt,  statusTxt, tempTxt, temp_minTxt, temp_maxTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt;

    RelativeLayout relaLay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Intent intent = getIntent();
        //grabbing the values from main activity so we can use them for the API call after
        CITY = intent.getStringExtra("City");
        choice = intent.getStringExtra("choose");
        Log.d("is it working the intent ", CITY);
        Log.d("is it working the intent ", choice);


        addressTxt = findViewById(R.id.address);
        statusTxt = findViewById(R.id.status);
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
    }



    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /* Showing the ProgressBar if the api takes awhile */
            findViewById(R.id.loader).setVisibility(View.VISIBLE);
            findViewById(R.id.errorText).setVisibility(View.GONE);
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

                String temp = main.getString("temp");
                double fah = Double.parseDouble(temp);
                //calculation for fahrenheight need to make a class better
                fah = (fah * 1.8) + 32;
                //rounding the value
                temp = ((int) Math.round(fah)) + " °F";
                        //+ "°C"
                // turn the tempMin into fahrenheit
               // String tempMin = "Min Temp: " + main.getString("temp_min") + "°C";
                double min =  Double.parseDouble(main.getString("temp_min"));
                min = (min * 1.8) + 32;
                String tempMin = "Min Temp: " + ((int) Math.round(min)) + "°F";

                // turn the max temp into fahrenheit
                //String tempMax = "Max Temp: " + main.getString("temp_max") + "°C";

                double max = Double.parseDouble(main.getString("temp_max"));
                max = (max * 1.8) + 32;
                String tempMax = "Max Temp: " + ((int) Math.round(max)) + "°F";
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
                tempTxt.setText(temp);
                temp_minTxt.setText(tempMin);
                temp_maxTxt.setText(tempMax);
//                sunriseTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
//                sunsetTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
//                windTxt.setText(windSpeed);
//                pressureTxt.setText(pressure);
//                humidityTxt.setText(humidity);

//                resultView.setText(windSpeed);
                /* Views populated, Hiding the loader, Showing the main design */
                findViewById(R.id.loader).setVisibility(View.GONE);


            } catch (JSONException e) {
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.errorText).setVisibility(View.VISIBLE);
            }

        }
    }
}