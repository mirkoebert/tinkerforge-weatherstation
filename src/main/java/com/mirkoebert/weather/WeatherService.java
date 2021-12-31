package com.mirkoebert.weather;

import com.mirkoebert.weather.openweather.OpenWeatherService;
import com.mirkoebert.weather.openweather.OpenWeatherWeather;
import com.mirkoebert.weather.tinkerforge.TinkerforgeWeatherMonitor;
import com.mirkoebert.weather.tinkerforge.TinkerforgeWeatherService;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final OpenWeatherService ows;
    private final TinkerforgeWeatherService tfs;
    private final TinkerforgeWeatherMonitor tfwm;

    public Weather getAggregatedWeather() {
        val w = new Weather();

        addWeatherFromOpenWeather(w);
        addWeatherFromTinkerforgeWeatherStation(w);
        w.setForecast(tfwm.getForeCast());

        return w;
    }

    private void addWeatherFromTinkerforgeWeatherStation(final com.mirkoebert.weather.Weather w) {
        final Weather w2 = tfs.getWeather();
        if (w2 != null) {
            w.setAirpressure(w2.getAirpressure());
            w.setHumidityIn(w2.getHumidityIn());
            w.setTempIn(w2.getTempIn());
        }
    }

    private void addWeatherFromOpenWeather(final com.mirkoebert.weather.Weather w) {
        final OpenWeatherWeather oww = ows.getWeather();
        if (oww != null) {
            w.setName(oww.getName());
            w.setDescription(oww.getDescription());
            w.setAirpressure(Optional.of(oww.getPressure()));
            w.setTempOut(oww.getTemp());
            w.setFeelsTemp(oww.getFeelsTemp());
            w.setHumidityOut(oww.getHumidity());
        }
    }

}
