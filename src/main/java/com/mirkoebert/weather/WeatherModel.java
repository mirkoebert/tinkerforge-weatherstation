package com.mirkoebert.weather;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * Model of the current weather, this mean the latest values of the weather station sensors.
 * @author mirkoebert
 *
 */
@Component
@Order(11)
public class WeatherModel {

    @Getter
    @Setter
    double tempIn = -273;
    @Getter
    @Setter
    long illumination = -1;
//    @Getter
//    @Setter
//    long date = System.currentTimeMillis();
    @Getter
    double airPressureQFE = -1;
    @Getter
    @Setter
    double humdidity = -1;

    @Autowired
    private AirPressurePointRepository apr;

    public void setAirPressureQFE(final double airPressureQFE) {
        apr.add(new AirpressurePoint(airPressureQFE));
        this.airPressureQFE = airPressureQFE;
    }

}
