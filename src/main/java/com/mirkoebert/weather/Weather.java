package com.mirkoebert.weather;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Weather {
    
    private String name = "AA";
    private double longitude = 12;//: 12.18,
    private double latitudec = -2; //": 53.98,
    private int altitude = 34; // 25,
    private float tempIn = 3;
    private float tempOut = 33;
    private float feelsTemp = 34;
    private float airpressure =1023;
    private float  humidityIn =34;
    private float  humidityOut = 55;
    private String description = "XXX";
    private String forecast = "DDDD";
    private long date = System.currentTimeMillis();


}