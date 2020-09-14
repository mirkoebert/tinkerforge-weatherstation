package com.mirkoebert.weather.tinkerforge;

import com.mirkoebert.weather.Weather;
import com.mirkoebert.weather.WeatherProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TinkerforgeWeatherService implements WeatherProvider{
    
    @Autowired
    private TinkerforgeWeather weatherModel;
    

    public TinkerforgeWeather getWeather2() {
        return weatherModel;
    }
    
    public Weather getWeather() {
        Weather weather = new Weather();
        weather.setAirpressure((float) weatherModel.getAirPressureQFE());
        weather.setTempIn((float) weatherModel.getTempIn());
        weather.setHumidityIn((float) weatherModel.getHumdidity());
        return weather;
    }
}
