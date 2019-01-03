package com.ebertp;


import lombok.Getter;
import lombok.Setter;

import org.springframework.stereotype.Component;

/**
 * Model of the current weather, this mean the latest values of the weather station sensors.
 * @author mirkoebert
 *
 */
@Component
public class WeatherModel {

    @Getter
    @Setter
    double tempIn;
    @Getter
    @Setter
    long illumination;
    @Getter
    @Setter
    long date;
    @Getter
    double airPressureQFE;
    @Getter
    @Setter
    double humdidity;

    @Getter
    private boolean alarm;

    private WeatherMonitor monitor;

    public WeatherModel() {
        monitor = new WeatherMonitor(this);
    }

    public void setAirPressureQFE(double airPressureQFE) {
        date = System.currentTimeMillis();
        this.airPressureQFE = airPressureQFE;
        AirPressurePointRepository.getINSTANCE().add(new AirpressurePoint(date, airPressureQFE));
    }



    /**
     * Weather forcast. 
     * @return Short forecast string to display on weather station's LSD.
     */
    public String getForecast() {
        String r = "";
        alarm = true;
        if (monitor.isHumidityAlarm()) {
            r = "Warnung: Luftfeuchtigkeit";
        } else if (monitor.isFrostAlarm()) {
            r = "Warnung: Frostgefahr";
        } else if (monitor.isStormAlarm()) {
            r = "Warnung: Sturm";
        } else if (monitor.isFireAlarm()) {
            r = "Warnung: Feuer";
        } else {
            r = monitor.getForeCast();
            alarm = false;
        }
        return r;
    }

    public String getAirpPressureTrend() {
        AirPressureTrend trend = monitor.getAirPressureTrend();
        if (trend == AirPressureTrend.falling) {
            return "fallend";
        } else if (trend == AirPressureTrend.stable) {
            return "gleichbleibend";
        } else {
            return "steigend";
        }
    }

}
