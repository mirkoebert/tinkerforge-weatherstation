package com.mirkoebert.weather.tinkerforge;

import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletBarometer.AirPressureListener;
import com.tinkerforge.TinkerforgeException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Air pressure listener implementing a Tinkerforge air pressure listener
 * (callback). Return the air pressure (QFE).
 *
 * @author mebert
 *
 */
@Slf4j
@AllArgsConstructor
final class AirPressureListenerX implements AirPressureListener {

    private final TinkerforgeWeather weatherModel;
    private final BrickletBarometer barometer;

    @Override
    public void airPressure(final int airPressure) {
        final double QFE = airPressure / 1000f;
        weatherModel.setAirPressureQFE(QFE);
        try {
            final double Tfe = barometer.getChipTemperature() / 100.0;
            weatherModel.setTempIn(Tfe);
        } catch (TinkerforgeException e) {
            log.error("Can't get chip temp.", e);
        }
    }

}
