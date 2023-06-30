package com.example.weatherapp;

public class CitiesItems {
    private String CITY;
    private String temperature;
    private String iconUrl;

    public String getCITY() {
        return CITY;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public CitiesItems(String CITY, String temperature, String image) {
        this.CITY = CITY;
        this.temperature = temperature;
        this.iconUrl = image;
    }
}
