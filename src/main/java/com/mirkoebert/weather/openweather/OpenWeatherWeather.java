package com.mirkoebert.weather.openweather;

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
public class OpenWeatherWeather {

    private String name;
    private float temp;
    private float feelsTemp;
    private float minTemp;
    private float maxTemp;
    private float pressure;
    private float humidity;
    private String description;

    @JsonProperty("main")
    private void unpackNestedMain(Map<Object, Object> brand) {
        temp = getFloatFromNestedObject(brand.get("temp"));
        minTemp = getFloatFromNestedObject(brand.get("temp_min"));
        maxTemp = getFloatFromNestedObject(brand.get("temp_max"));
        pressure = getFloatFromNestedObject(brand.get("pressure"));
        humidity = getFloatFromNestedObject(brand.get("humidity"));
        feelsTemp = getFloatFromNestedObject(brand.get("feels_like"));
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("weather")
    private void unpackNestedWeather(ArrayList<?> brand) {
        log.debug(brand.getClass().getName().toString());
        Map<Object, Object> w = (Map<Object, Object>) brand.get(0);
        description = w.get("description").toString();
    }

    private float getFloatFromNestedObject(final Object traw) {
        log.debug(traw.toString());
        if (traw instanceof Number) {
            return Float.valueOf(traw.toString());
        }
        log.warn("Can't convert Json " + traw.toString() + " to float.");
        return -1;
    }

}
