package com.mirkoebert.openweather;

import com.mirkoebert.openweather.send.Sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(130)
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
        if ((owm == null)||(now-lastWeatherUpdateAt> 60*1000)) {
            owm = wows.getWeatherForWeatherDStationCoordinates();
            lastWeatherUpdateAt = System.currentTimeMillis();
        }
        return owm ;
    }
}
