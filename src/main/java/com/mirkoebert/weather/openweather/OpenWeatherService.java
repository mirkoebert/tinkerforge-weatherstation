package com.mirkoebert.weather.openweather;

import com.mirkoebert.weather.openweather.http.Sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Order(130)
@Slf4j
public class OpenWeatherService {

    @Autowired
    private Sender wows;

    private long lastWeatherUpdateAt = 0;
    private OpenWeatherWeatherStation ws = null;
    private OpenWeatherWeather owm = null;

    public OpenWeatherWeatherStation getStation(){
        if (ws == null) {
            ws = wows.getWeatherStationFromOpenWeather();
        }
        return ws;
    }

    public OpenWeatherWeather getWeather() {
        long now = System.currentTimeMillis();
        OpenWeatherWeather owmNew = null;
        if ((owm == null)||(now-lastWeatherUpdateAt> 60*1000)) {
            owmNew = wows.getWeatherForWeatherDStationCoordinates();
            lastWeatherUpdateAt = System.currentTimeMillis();
        }
        if (owmNew != null) {
            owm = owmNew;
        } else {
            log.warn("Use old OW data");
        }
        return owm ;
    }
}
