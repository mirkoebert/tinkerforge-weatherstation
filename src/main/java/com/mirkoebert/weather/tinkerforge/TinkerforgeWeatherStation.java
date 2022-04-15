package com.mirkoebert.weather.tinkerforge;

import com.mirkoebert.controllerIn.WeatherViewLcd24x4;
import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.BrickMaster;
import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.IPConnection;
import com.tinkerforge.IPConnection.EnumerateListener;
import com.tinkerforge.NetworkException;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TinkerforgeException;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents all Tinkerforge weather station components (sensors).
 * 
 * @author mirkoebert
 *
 */
@Slf4j
@Component
@Order(20)
public class TinkerforgeWeatherStation implements EnumerateListener {

    private static final int callbackPeriodMsec = 30002;
    private static final String HOST = "localhost";
    private static final int PORT = 4223;
    private IPConnection ipcon = null;

    @Value("${info.app.name}")
    private String applicationName;

    @Value("${info.app.version}")
    private String buildVersion;

    @Getter
    private final LocalDateTime startDate = LocalDateTime.now();

    @Value("${weatherstation.mode.night-mode}")
    private boolean nightmode;

    @Value("${weatherstation.mode.alarm-flashing}")
    private boolean alarmflashingmode;

    private final TinkerforgeWeather weatherModel;
    private final TinkerforgeWeatherMonitor weatherMonitor;

    @Value("${tinkerforge.enable}")
    private boolean tinkerforgeEnable;

    public TinkerforgeWeatherStation(final TinkerforgeWeather w, final TinkerforgeWeatherMonitor f) {
        weatherModel = w;
        weatherMonitor = f;
        // if (tinkerforgeEnable) {
        log.info("Initiate Tinkerforge tinkerforgeEnable" + tinkerforgeEnable);
        ipcon = new IPConnection();
        try {
            ipcon.connect(HOST, PORT);
            ipcon.addEnumerateListener(this);
            // configure all bricks and bricklets
            ipcon.enumerate();
        } catch (NetworkException | AlreadyConnectedException | NotConnectedException e) {
            log.error(e.getLocalizedMessage(), e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                //
            }
        }));
        // } else {
        // log.info("Dry run mode - no Tinkerforge brick initialisation
        // tinkerforgeEnable" + tinkerforgeEnable);
        // }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("Shutdown");
        ipcon.disconnect();
    }

    @Override
    public void enumerate(
            final String uid,
            final String connectedUid,
            final char position,
            final short[] hardwareVersion,
            final short[] firmwareVersion,
            final int deviceIdentifier,
            final short enumerationType) {
        switch (deviceIdentifier) {
            case 13:
                final BrickMaster master = new BrickMaster(uid, ipcon);
                try {
                    master.disableStatusLED();
                } catch (TinkerforgeException ex) {
                    log.error(ex.getLocalizedMessage());
                }
                break;
            case 17:
                // String UIDred = uid;
                break;
            case 21:
                final BrickletAmbientLight ambientLightBrick = new BrickletAmbientLight(uid, ipcon);
                try {
                    ambientLightBrick.setIlluminanceCallbackPeriod(callbackPeriodMsec);
                    ambientLightBrick.addIlluminanceListener(new IlluminanceListenerX(weatherModel));
                } catch (Exception ex) {
                    log.error(ex.getLocalizedMessage());
                }
                break;
            case 212:
                final BrickletLCD20x4 lcd = new BrickletLCD20x4(uid, ipcon);
                try {
                    lcd.backlightOff();
                    lcd.clearDisplay();
                    lcd.backlightOn();
                    lcd.writeLine((short) 1, (short) 1, applicationName);
                    lcd.writeLine((short) 2, (short) 1, buildVersion);
                    val lcdView = new WeatherViewLcd24x4(lcd, weatherModel, weatherMonitor);
                    lcdView.setNightmode(nightmode);
                    lcdView.setAlarmflashingmode(alarmflashingmode);
                } catch (TinkerforgeException ex) {
                    log.error(ex.getLocalizedMessage());
                }

                break;
            case 221:
                final BrickletBarometer barBrick = new BrickletBarometer(uid, ipcon);
                try {
                    barBrick.setAirPressureCallbackPeriod(callbackPeriodMsec);
                } catch (Exception ex) {
                    log.error(ex.getLocalizedMessage());
                }
                barBrick.addAirPressureListener(new AirPressureListenerX(weatherModel, barBrick));
                break;
            case 27:
                final BrickletHumidity humBrick = new BrickletHumidity(uid, ipcon);
                final HumidityListenerX humListener = new HumidityListenerX(weatherModel);
                try {
                    humBrick.setHumidityCallbackPeriod(callbackPeriodMsec);
                } catch (Exception ex) {
                    log.error(ex.getLocalizedMessage());
                }
                humBrick.addHumidityListener(humListener);
                break;
            default:
                String hardwareVersionString = hardwareVersion[0] + "." + hardwareVersion[1];
                String firmwareVersionString = firmwareVersion[0] + "." + firmwareVersion[1];
                log.warn("Unsupported Brick or Bricklet. Uid: " + uid + "  " + connectedUid + " " + position + " "
                        + hardwareVersionString + " " + firmwareVersionString + " " + deviceIdentifier + " "
                        + enumerationType);
                // 2GewbC 0 0 [S@150d707 [S@197219d 17 1
                break;
        }
    }
}
