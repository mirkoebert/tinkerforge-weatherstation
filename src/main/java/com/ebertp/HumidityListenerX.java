package com.ebertp;

import com.tinkerforge.BrickletHumidity.HumidityListener;

import lombok.extern.slf4j.Slf4j;

/**
 * Humidity listener that also calculates warning if the relative humidity is
 * out of human comfort zone. The Warning is displayed on the LCD display.
 *
 * @author mebert
 *
 */
@Slf4j
class HumidityListenerX implements HumidityListener {

    private final WeatherModel weaterModel;

    public HumidityListenerX(final WeatherModel weaterModel) {
        this.weaterModel = weaterModel;
    }

    @Override
    public void humidity(final int humidity) {
        final double h = humidity / 10.0;
        weaterModel.setHumdidity(h);
        log.info("Relative Humidity: " + h + " %RH");
    }

}