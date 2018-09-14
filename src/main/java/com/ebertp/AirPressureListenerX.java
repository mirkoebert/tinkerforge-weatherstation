package com.ebertp;

import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletBarometer.AirPressureListener;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * Air pressure listener implementing a Tinkerforge air pressure listener (callback). Return the air
 * pressure correct by temperature (from chip) and current high over sea level.
 *
 * @author mebert
 *
 */
final class AirPressureListenerX implements AirPressureListener {

    private final BrickletBarometer barometer;
    private WeatherModel weatherModel;
    final double Tg = 0.0065;
    final double hoeheMessstation = 23;

    public AirPressureListenerX(WeatherModel m, final BrickletBarometer barometer) {
        this.weatherModel = m;
        this.barometer = barometer;
    }

    @Override
    public void airPressure(final int airPressure) {
        final float QFE = airPressure / 1000f;
        weatherModel.setAirPressure(QFE);

        try {
            double Tfe = barometer.getChipTemperature() / 100.0;
            weatherModel.setTempIn(Tfe);
            // double QFF = QFE / Math.pow(1 - Tg * H / (273.15 + Tfe + Tg * H), 0.034163 / Tg);
        } catch (TimeoutException | NotConnectedException e) {
            e.printStackTrace();
        }
    }

}
