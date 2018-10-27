package com.ebertp;

import com.tinkerforge.BrickletAmbientLight.IlluminanceListener;

import lombok.extern.slf4j.Slf4j;

/**
 * Illumination listener that also switches the back light of the LCD display.
 * 
 * @author mebert
 *
 */
@Slf4j
final class IlluminanceListenerX implements IlluminanceListener {

    private final WeatherModel weatherModel;

    public IlluminanceListenerX(WeatherModel weaterModel) {
        this.weatherModel = weaterModel;
    }

    @Override
    public void illuminance(final int illuminance) {
        final long ill = Math.round(illuminance / 10.0);
        weatherModel.setIllumination(ill);
        log.info("Illuminance: " + ill);
    }

}
