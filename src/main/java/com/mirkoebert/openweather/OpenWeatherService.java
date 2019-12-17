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

    private WeatherStation ws = null;


    public WeatherStation getStation(){
        if (ws == null) {
            ws = wows.getWeatherStationFromOpenWeather();
        }
        return ws;
    }
}
