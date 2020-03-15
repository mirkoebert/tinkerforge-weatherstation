package com.mirkoebert.weather.tinkerforge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TinkerforgeWeatherService {
    
    @Autowired
    private TinkerforgeWeather weatherModel;
    

    public TinkerforgeWeather getWeather2() {
        return weatherModel;
    }
}
