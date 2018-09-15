package com.ebertp;

import org.springframework.stereotype.Component;

@Component
public final class WeatherModel {

    double tempIn, tempOut;
    long illumination;
    long date;
    double airPressure, humdidity;
    private WeatherAnalyzer monitor;

    public WeatherModel() {
        monitor = new WeatherAnalyzer(this);
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getIllumination() {
        return illumination;
    }

    public void setIllumination(long illumination) {
        this.illumination = illumination;
    }

    public double getTempIn() {
        return tempIn;
    }

    public void setTempIn(double tempIn) {
        this.tempIn = tempIn;
    }

    public double getTempOut() {
        return tempOut;
    }

    public void setTempOut(float tempOut) {
        this.tempOut = tempOut;
    }

    public double getAirPressure() {
        date = System.currentTimeMillis();
        return airPressure;
    }

    public void setAirPressure(double airPressure) {
        this.airPressure = airPressure;
    }

    public double getHumdidity() {
        return humdidity;
    }

    public void setHumdidity(double humdidity) {
        this.humdidity = humdidity;
    }

    public String getForecast() {
        return monitor.getForeCast();
    }

    public String getWarning() {
        String r = "";
        if (monitor.isHumidityAlarm()) {
            r = "Luftfeuchtigkeit";
        } else if (monitor.isFrostAlarm()) {
            r = "Frostgefahr";
        } else if (monitor.isStormAlarm()) {
            r = "Sturm";
        } else if (monitor.isFireAlarm()) {
            r = "Feuer";
        }
        return r;

    }

}
