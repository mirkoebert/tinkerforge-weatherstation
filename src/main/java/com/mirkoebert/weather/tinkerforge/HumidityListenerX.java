package com.mirkoebert.weather.tinkerforge;

import com.tinkerforge.BrickletHumidity.HumidityListener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Humidity listener that also calculates warning if the relative humidity is
 * out of human comfort zone. The Warning is displayed on the LCD display.
 *
 * @author mebert
 *
 */
@Slf4j
@AllArgsConstructor
class HumidityListenerX implements HumidityListener {

    private final TinkerforgeWeather weatherModel;

    @Override
    public void humidity(final int humidity) {
        final double h = humidity / 10.0;
        weatherModel.setHumdidity(h);
        log.debug("Relative Humidity: " + h + " %RH");
    }

}