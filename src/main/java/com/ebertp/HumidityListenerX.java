package com.ebertp;

import com.tinkerforge.BrickletHumidity.HumidityListener;

/**
 * Humidity listener that also calculates warning if the relative humidity is out of human comfort
 * zone. The Warning is displayed on the LCD display.
 *
 * @author mebert
 *
 */
class HumidityListenerX implements HumidityListener {

    private WeatherModel m;

    public HumidityListenerX(final WeatherModel m) {
        this.m = m;
    }

    @Override
    public void humidity(final int humidity) {
        final double h = humidity / 10.0;
        m.setHumdidity(h);
        System.out.println("Relative Humidity: " + h + " %RH");
    }

}