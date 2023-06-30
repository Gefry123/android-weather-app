package com.example.weatherapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private final Context context;
    private ArrayList<CitiesItems> citiesItems;

    public final String TAG = "RecyclerViewAdapter";

    public RecyclerViewAdapter(Context context, ArrayList<CitiesItems> citiesItems){
        this.context = context;
        this.citiesItems = citiesItems;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "Inside onCreateViewHolder method");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d(TAG, "Inside onBindViewHolder method");
        CitiesItems cities = citiesItems.get(position);
        holder.bind(cities);
    }

    @Override
    public int getItemCount() {
        return citiesItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView cityName;
        private TextView temperature;
        private ImageView temperature_icon;
        public final String TAG = "RecyclerViewAdapter";
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "Inside MyViewHolder method");

            cityName = itemView.findViewById(R.id.item_city);
            temperature = itemView.findViewById(R.id.item_temperature);
            temperature_icon = itemView.findViewById(R.id.temp_icon);

        }
        public void bind(CitiesItems citiesTemp){
            Log.d(TAG, "Inside bind method" + citiesTemp.getCITY() + " " + citiesTemp.getTemperature() + " " + citiesTemp.getIconUrl());
            cityName.setText(citiesTemp.getCITY());
            temperature.setText(citiesTemp.getTemperature());
            String iconUrl = "https://openweathermap.org/img/wn/" + citiesTemp.getIconUrl() + ".png";
            if (iconUrl != null && !iconUrl.isEmpty()) {
                Picasso.get().load(iconUrl).into(temperature_icon);
            }
        }
    }
}
