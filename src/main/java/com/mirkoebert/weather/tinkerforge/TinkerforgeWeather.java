package com.mirkoebert.weather.tinkerforge;

import com.mirkoebert.weather.openweather.http.ObserverableWeather;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Model of the current weather, this mean the latest values of the weather
 * station sensors.
 * 
 * @author mirkoebert
 *
 */
@Component
@RequiredArgsConstructor
public class TinkerforgeWeather implements ObserverableWeather {

    @Getter
    @Setter
    private double tempIn = -273;
    @Getter
    @Setter
    private long illumination = -1;
    @Getter
    @Setter
    private double humdidity = -1;
    @Getter
    private double airPressureQFE = -1;

    private final AirPressurePointRepository apr;

    public void setAirPressureQFE(final double airPressureQFE) {
        apr.add(new AirpressurePoint(airPressureQFE));
        this.airPressureQFE = airPressureQFE;
    }

}
