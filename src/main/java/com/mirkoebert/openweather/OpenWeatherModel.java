package com.mirkoebert.openweather;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class OpenWeatherModel {

    private String name;
    private float temp;
    private float feelsTemp;
    private float minTemp;
    private float maxTemp;
    private float pressure;
    private float  humidity;
    private String description;
    
    @SuppressWarnings("unchecked")
    @JsonProperty("main")
    private void unpackNestedMain(Map<Object,Object> brand) {
        temp = getFloatFromNestedObject(brand.get("temp"));
        minTemp = getFloatFromNestedObject(brand.get("temp_min"));
        maxTemp = getFloatFromNestedObject(brand.get("temp_max"));
        pressure = getFloatFromNestedObject(brand.get("pressure"));
        humidity = getFloatFromNestedObject(brand.get("humidity"));
    }
    
    @SuppressWarnings("unchecked")
    @JsonProperty("weather")
    private void unpackNestedWeather(ArrayList brand) {
        log.debug(brand.getClass().getName().toString());
        Map<Object,Object> w = (Map<Object, Object>) brand.get(0);
        description = getStringFromNestedObject(w.get("description"));
    }
    
    private float getFloatFromNestedObject(final Object traw) {
        log.debug(traw.toString());
        if (traw instanceof Number) {
            return Float.valueOf(traw.toString());
        } 
        log.warn("Can't convert Json " + traw.toString() + " to float.");
        return -1;
    }
    
    private String getStringFromNestedObject(final Object traw) {
        return traw.toString();
    }
}