package com.example.smartgarden;

import com.google.gson.annotations.SerializedName;

public class Weather {
    private String description;

    public String getDescription() {
        return description;
    }



    @SerializedName("icon")
    private String icon;



    public String getIcon() {
        return icon;
    }
}

