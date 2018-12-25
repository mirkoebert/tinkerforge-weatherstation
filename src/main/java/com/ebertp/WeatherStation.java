package com.ebertp;

import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.IPConnection;
import com.tinkerforge.IPConnection.EnumerateListener;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public final class WeatherStation implements EnumerateListener {

    private static final String HOST = "localhost";
    private static final int PORT = 4223;

    // TODO get UIDs from station
    private String UIDamb = "mhH";
    private String UIDbar = "k6y";
    // private static final String UIDmas = "6Ka5bE";
    private String UIDhum = "nBj";
    private String UIDlcd = "odC";

    private final BrickletLCD20x4 lcd;
    @Getter
    private WeatherModel weatherModel;

    private final IPConnection ipcon;

    @Autowired
    public WeatherStation(@Value("${info.app.name}") String applicationName,
            @Value("${info.app.version}") String buildVersion) throws Exception {
        ipcon = new IPConnection();
        

        final BrickletHumidity humBrick = new BrickletHumidity(UIDhum, ipcon);
        lcd = new BrickletLCD20x4(UIDlcd, ipcon);
        final BrickletBarometer barBrick = new BrickletBarometer(UIDbar, ipcon);
        final BrickletAmbientLight ambientLightBrick = new BrickletAmbientLight(UIDamb, ipcon);

        ipcon.connect(HOST, PORT);
        ipcon.addEnumerateListener(this);
        ipcon.enumerate();

        // start message
        lcd.backlightOff();
        lcd.clearDisplay();
        lcd.backlightOn();
        lcd.writeLine((short) 0, (short) 0, applicationName);
        lcd.writeLine((short) 0, (short) 0, buildVersion);
        log.info(applicationName + " " + buildVersion);

        weatherModel = new WeatherModel();

        final HumidityListenerX humListener = new HumidityListenerX(weatherModel);

        humBrick.setHumidityCallbackPeriod(30003);
        humBrick.addHumidityListener(humListener);

        barBrick.setAirPressureCallbackPeriod(30002);
        barBrick.addAirPressureListener(new AirPressureListenerX(weatherModel, barBrick));

        ambientLightBrick.setIlluminanceCallbackPeriod(30001);
        ambientLightBrick.addIlluminanceListener(new IlluminanceListenerX(weatherModel));

        new WeatherMonitor(weatherModel);

        new WeatherViewLcd24x4(weatherModel, lcd);
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


    @Override
    public void enumerate(String uid, String connectedUid, char position, short[] hardwareVersion,
            short[] firmwareVersion, int deviceIdentifier, short enumerationType) {

        log.info("Auto configuration, collect all needed Brick and Bricklets Uids.");
        switch (deviceIdentifier) {
        case 13:
            UIDamb = uid;
            break;
        case 17:
            UIDamb = uid;
            break;
        case 21:
            UIDamb = uid;
            break;
        case 212:
            UIDlcd = uid;
            break;
        case 221:
            UIDbar = uid;
            break;
        case 27:
            UIDhum = uid;
            break;
        default:
            log.warn("Unsupported Brick or Bricklet. Uid: "+uid + "  " +connectedUid+ " "+ position+ " "+hardwareVersion+" "+firmwareVersion+ " "+ deviceIdentifier+ " "+ enumerationType);
            //              2GewbC       0                   0           [S@150d707          [S@197219d                      17                    1
            break;
        }
    }
}