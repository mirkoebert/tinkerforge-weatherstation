package com.mirkoebert.weather;

import com.mirkoebert.weather.openweather.OpenWeatherService;
import com.mirkoebert.weather.openweather.OpenWeatherWeather;
import com.mirkoebert.weather.tinkerforge.TinkerforgeWeather;
import com.mirkoebert.weather.tinkerforge.TinkerforgeWeatherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {
    
    @Autowired
    private OpenWeatherService ows;
    @Autowired
    private TinkerforgeWeatherService tfs;
    
    public Weather getWeather(){
        Weather w = new Weather();
        OpenWeatherWeather oww = ows.getWeather();
        w.setName(oww.getName());
        w.setDescription(oww.getDescription());
        w.setAirpressure(oww.getPressure());
        w.setTempOut(oww.getTemp());
        w.setFeelsTemp(oww.getFeelsTemp());
        w.setHumidityOut(oww.getHumidity());
        
        TinkerforgeWeather w2 = tfs.getWeather();
        double pressure = w2.getAirPressureQFE();
        if (pressure > 0) {
            w.setAirpressure((float)pressure );
        }
        w.setHumidityIn((float) (w2.getHumdidity()));
        w.setTempIn((float) w2.getTempIn());
        
        return w;
    }

}
