package com.mirkoebert.weather.tinkerforge;

import org.springframework.stereotype.Service;

@Service
public class TinkerforgeWeatherService {
    
    public TinkerforgeWeather getWeather() {
        return new TinkerforgeWeather();
    }

}
