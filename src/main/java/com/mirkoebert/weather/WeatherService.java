package com.mirkoebert.weather;

import com.mirkoebert.weather.openweather.OpenWeatherService;
import com.mirkoebert.weather.openweather.OpenWeatherWeather;
import com.mirkoebert.weather.tinkerforge.TinkerforgeWeatherMonitor;
import com.mirkoebert.weather.tinkerforge.TinkerforgeWeatherService;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WeatherService {

    private final OpenWeatherService ows;
    private final TinkerforgeWeatherService tfs;
    private final TinkerforgeWeatherMonitor tfwm;

    public Weather getWeather() {
        Weather w = new Weather();

        OpenWeatherWeather oww = ows.getWeather();
        if (oww != null) {
            w.setName(oww.getName());
            w.setDescription(oww.getDescription());
            w.setAirpressure(Optional.of(oww.getPressure()));
            w.setTempOut(oww.getTemp());
            w.setFeelsTemp(oww.getFeelsTemp());
            w.setHumidityOut(oww.getHumidity());
        }

        Weather w2 = tfs.getWeather();
        if (w2 != null) {
            w.setAirpressure(w2.getAirpressure());
            w.setHumidityIn(w2.getHumidityIn());
            w.setTempIn(w2.getTempIn());
        }

        w.setForecast(tfwm.getForeCast());
        return w;
    }

}
