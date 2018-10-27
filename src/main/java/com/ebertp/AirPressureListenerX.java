package com.ebertp;

import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletBarometer.AirPressureListener;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

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

    private final WeatherModel weatherModel;
    private final BrickletBarometer barometer;
    
    @Override
    public void airPressure(final int airPressure) {
        final float QFE = airPressure / 1000f;
        weatherModel.setAirPressure(QFE);
        try {
            double Tfe = barometer.getChipTemperature() / 100.0;
            weatherModel.setTempIn(Tfe);
        } catch (TimeoutException | NotConnectedException e) {
            log.error("Chip temp", e);
        }
    }

}
