package com.example.smartgarden;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {
    private Main main;
    private List<Weather> weather;

    public Main getMain() {
        return main;
    }

    public List<Weather> getWeather() {
        return weather;
    }
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

}

