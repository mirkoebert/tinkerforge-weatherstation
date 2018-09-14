package com.ebertp;

import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import org.springframework.stereotype.Component;

@Component
public final class WeatherStation {

    private static final String HOST = "localhost";
    private static final int PORT = 4223;
    private static final String UIDamb = "mhH";
    private static final String UIDbar = "k6y";
    // private static final String UIDmas = "6Ka5bE";
    private static final String UIDhum = "nBj";
    private static final String UIDlcd = "odC";

    private final BrickletLCD20x4 lcd;
    private WeatherModel weatherModel;

    private final IPConnection ipcon;

    public WeatherModel getModell() {
        return weatherModel;
    }

    public WeatherStation() throws Exception {
        ipcon = new IPConnection();

        final BrickletHumidity humBrick = new BrickletHumidity(UIDhum, ipcon);
        lcd = new BrickletLCD20x4(UIDlcd, ipcon);
        final BrickletBarometer barBrick = new BrickletBarometer(UIDbar, ipcon);
        final BrickletAmbientLight ambientLightBrick = new BrickletAmbientLight(UIDamb, ipcon);

        ipcon.connect(HOST, PORT);
        // start message
        lcd.backlightOff();
        lcd.clearDisplay();
        lcd.writeLine((short) 0, (short) 0, " Weather Station");
        lcd.backlightOn();

        weatherModel = new WeatherModel();

        final HumidityListenerX humListener = new HumidityListenerX(weatherModel);

        humBrick.setHumidityCallbackPeriod(30003);
        humBrick.addHumidityListener(humListener);

        barBrick.setAirPressureCallbackPeriod(30002);
        barBrick.addAirPressureListener(new AirPressureListenerX(weatherModel, barBrick));

        ambientLightBrick.setIlluminanceCallbackPeriod(30001);
        ambientLightBrick.addIlluminanceListener(new IlluminanceListenerX(weatherModel));

        new WeatherMonitor(weatherModel);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                //
            }
        }));
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("Shutdown");
        try {
            lcd.backlightOff();
            lcd.clearDisplay();
        } catch (TimeoutException | NotConnectedException e) {
            e.printStackTrace();
        }
        ipcon.disconnect();
    }
}