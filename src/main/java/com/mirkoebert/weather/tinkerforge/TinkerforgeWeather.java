package com.mirkoebert.weather.tinkerforge;


import com.mirkoebert.weather.openweather.http.ObserverableWeather;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * Model of the current weather, this mean the latest values of the weather station sensors.
 * @author mirkoebert
 *
 */
@Component
public class TinkerforgeWeather implements ObserverableWeather{

    @Getter
    @Setter
    double tempIn = -273;
    @Getter
    @Setter
    long illumination = -1;
    @Getter
    @Setter
    double humdidity = -1;
    @Getter
    double airPressureQFE = -1;


    private final AirPressurePointRepository apr;
    
    public TinkerforgeWeather(final AirPressurePointRepository apr) {
        this.apr = apr;
    }

    public void setAirPressureQFE(final double airPressureQFE) {
        apr.add(new AirpressurePoint(airPressureQFE));
        this.airPressureQFE = airPressureQFE;
    }


}
