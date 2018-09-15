package com.ebertp;

import com.tinkerforge.BrickletHumidity.HumidityListener;

import lombok.extern.slf4j.Slf4j;

/**
 * Humidity listener that also calculates warning if the relative humidity is out of human comfort
 * zone. The Warning is displayed on the LCD display.
 *
 * @author mebert
 *
 */
@Slf4j
class HumidityListenerX implements HumidityListener {

    private final WeatherModel weatherModel;

    public HumidityListenerX(final WeatherModel weatherModel) {
        this.weatherModel = weatherModel;
    }

    @Override
    public void humidity(final int humidity) {
        final double h = humidity / 10.0;
        weatherModel.setHumdidity(h);
        log.info("Relative Humidity: " + h + " %RH");
    }

}