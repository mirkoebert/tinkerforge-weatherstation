package com.mirkoebert.weather.tinkerforge;

import com.mirkoebert.weather.Weather;
import com.mirkoebert.weather.WeatherProvider;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TinkerforgeWeatherService implements WeatherProvider{
    
    private final TinkerforgeWeather weatherModel;
    
    
    public Weather getWeather() {
        Weather weather = new Weather();
        weather.setAirpressure(Optional.of((float)weatherModel.getAirPressureQFE()));
        weather.setTempIn((float) weatherModel.getTempIn());
        weather.setHumidityIn((float) weatherModel.getHumdidity());
        return weather;
    }
}
