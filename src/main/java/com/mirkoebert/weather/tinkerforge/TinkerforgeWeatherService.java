package com.mirkoebert.weather.tinkerforge;

import com.mirkoebert.weather.Weather;
import com.mirkoebert.weather.WeatherProvider;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
public class TinkerforgeWeatherService implements WeatherProvider {

    private final TinkerforgeWeather weatherModel;

    @Override
    public Weather getWeather() {
        val weather = new Weather();
        weather.setAirpressure(Optional.of((float) weatherModel.getAirPressureQFE()));
        weather.setTempIn((float) weatherModel.getTempIn());
        weather.setHumidityIn((float) weatherModel.getHumdidity());
        return weather;
    }
}
