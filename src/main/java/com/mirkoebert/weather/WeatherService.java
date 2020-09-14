package com.mirkoebert.weather;

import com.mirkoebert.weather.openweather.OpenWeatherService;
import com.mirkoebert.weather.openweather.OpenWeatherWeather;
import com.mirkoebert.weather.tinkerforge.TinkerforgeWeatherMonitor;
import com.mirkoebert.weather.tinkerforge.TinkerforgeWeatherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    @Autowired
    private OpenWeatherService ows;
    @Autowired
    private TinkerforgeWeatherService tfs;
    @Autowired
    private TinkerforgeWeatherMonitor tfwm;

    public Weather getWeather(){
        Weather w = new Weather();

        OpenWeatherWeather oww = ows.getWeather();
        if (oww != null) {
            w.setName(oww.getName());
            w.setDescription(oww.getDescription());
            w.setAirpressure(oww.getPressure());
            w.setTempOut(oww.getTemp());
            w.setFeelsTemp(oww.getFeelsTemp());
            w.setHumidityOut(oww.getHumidity());
        }

        Weather w2 = tfs.getWeather();
        if (w2 != null) {
            final float pressure = w2.getAirpressure();
            if (pressure > 0) {
                w.setAirpressure((float)pressure );
            }
            w.setHumidityIn(( w2.getHumidityIn()));
            w.setTempIn(w2.getTempIn());
        }

        w.setForecast(tfwm.getForeCast());
        return w;
    }

}
