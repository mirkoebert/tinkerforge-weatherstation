package com.ebertp;

import lombok.Getter;
import lombok.Setter;

/**
 * Model of the current weather, this mean the latest values of the weather station sensors.
 * @author mirkoebert
 *
 */
public class WeatherModel {

    @Getter
    @Setter
    double tempIn, tempOut;
    @Getter
    @Setter
    long illumination;
    @Getter
    @Setter
    long date;
    double airPressure;
    @Getter
    @Setter
    double humdidity;

    private WeatherMonitor monitor;

    public WeatherModel() {
        monitor = new WeatherMonitor(this);
    }

    public double getAirPressure() {
        date = System.currentTimeMillis();
        return airPressure;
    }

    public void setAirPressure(double airPressure) {
        this.airPressure = airPressure;
    }

    /**
     * Weather forcast. 
     * @return Short forecast string to display on weather station's LSD.
     */
    public String getForecast() {
        String r = "";
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
        }
        return r;
    }

}
