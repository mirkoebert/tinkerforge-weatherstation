package com.mirkoebert.openweather;

import com.mirkoebert.openweather.send.Sender;

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
    private OpenWeatherModel owm = null;

    public OpenWeatherWeatherStation getStation(){
        if (ws == null) {
            ws = wows.getWeatherStationFromOpenWeather();
        }
        return ws;
    }

    public OpenWeatherModel getOpenWeatherModel() {
        long now = System.currentTimeMillis();
        OpenWeatherModel owmNew = null;
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
