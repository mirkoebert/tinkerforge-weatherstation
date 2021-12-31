package com.mirkoebert.weather.openweather;

import com.mirkoebert.weather.openweather.http.OpenWeatherWeatherStation;
import com.mirkoebert.weather.openweather.http.Sender;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenWeatherService {

    private final Sender owsender;

    private long lastWeatherUpdateAt = 0;
    private OpenWeatherWeatherStation ws = null;
    private OpenWeatherWeather owm = null;

    public OpenWeatherWeatherStation getStation() {
        if (ws == null) {
            ws = owsender.getWeatherStationFromOpenWeather();
        }
        return ws;
    }

    @Scheduled(initialDelay = 240000, fixedDelay = 600000)
    public OpenWeatherWeather getWeather() {
        final long now = System.currentTimeMillis();
        OpenWeatherWeather owmNew = null;
        if ((owm == null) || (now - lastWeatherUpdateAt > 60 * 1000)) {
            owmNew = owsender.getWeatherForWeatherDStationCoordinates();
            lastWeatherUpdateAt = System.currentTimeMillis();
        }
        if (owmNew != null) {
            owm = owmNew;
        } else {
            log.warn("Use old OW data");
        }
        return owm;
    }
}
