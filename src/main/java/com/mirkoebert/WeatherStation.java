package com.mirkoebert;

import com.mirkoebert.weather.WeatherModel;
import com.mirkoebert.weather.WeatherMonitor;
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

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.Getter;
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
public final class WeatherStation implements EnumerateListener {

    private static final int callbackPeriodMsec = 30002;
    private static final String HOST = "localhost";
    private static final int PORT = 4223;
    private IPConnection ipcon = null;

    @Getter
    @Value("${info.app.name}")
    private String applicationName;

    @Getter
    @Value("${info.app.version}")
    private String buildVersion;

    @Getter
    private final Date startDate = new Date();

    @Value("${weatherstation.mode.nightmode}")
    private boolean nightmode;

    @Value("${weatherstation.mode.alarmfashing}")
    private boolean alarmflashingmode;

    @Getter
    @Value("${weatherstation.position.latitude}")
    private String latitude;

    @Getter
    @Value("${weatherstation.position.longitude}")
    private String longitude;

    @Getter
    @Value("${weatherstation.position.altitude}")
    private String altitude;

    private WeatherModel weatherModel;
    private WeatherMonitor weatherMonitor;

    @Value("${tinkerforge.enable}")
    private boolean tinkerforgeEnable
    ;

    public WeatherStation(WeatherModel w, WeatherMonitor f)  {
        weatherModel = w;
        weatherMonitor = f;
        if (!tinkerforgeEnable) {
            log.info("Dry run mode - no brick initialisation");
        } else {
            ipcon = new IPConnection();
            try {
                ipcon.connect(HOST, PORT);
                ipcon.addEnumerateListener(this);
                // configure all bricks and bricklets
                ipcon.enumerate();
            } catch (NetworkException | AlreadyConnectedException | NotConnectedException e) {
                log.error(e.getLocalizedMessage(),e);
            }


            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    //
                }
            }));
        }
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("Shutdown");
        ipcon.disconnect();
    }


    @Override
    public void enumerate(String uid, String connectedUid, char position, short[] hardwareVersion,
            short[] firmwareVersion, int deviceIdentifier, short enumerationType) {
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
                //String UIDred = uid;
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
                    lcd.writeLine((short) 0, (short) 0, applicationName);
                    lcd.writeLine((short) 1, (short) 0, buildVersion);
                    WeatherViewLcd24x4 lcdView = new WeatherViewLcd24x4(lcd, weatherModel, weatherMonitor);
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
                String hardwareVersionString = hardwareVersion[0] +"."+ hardwareVersion[1];
                String firmwareVersionString = firmwareVersion[0] +"."+ firmwareVersion[1];
                log.warn("Unsupported Brick or Bricklet. Uid: " + uid + "  " + connectedUid + " " + position+ " " + hardwareVersionString + " " + firmwareVersionString + " " + deviceIdentifier+ " " + enumerationType);
                //              2GewbC       0                   0           [S@150d707          [S@197219d                      17                    1
                break;
        }
    }       
}
