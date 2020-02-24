package com.mirkoebert.weather.openweather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherWeatherStation {
    
    private String id; //": "5df37e046c634e00011dff22",
    private String name; //": "Klingendorf",
    private double longitude;//: 12.18,
    private double latitude; //": 53.98,
    private int altitude; // 25,

}
