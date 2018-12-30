package com.ebertp;

import com.tinkerforge.BrickMaster;
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

    private static final int callbackPeriodMsec = 30002;
    private static final String HOST = "localhost";
    private static final int PORT = 4223;

    @Getter
    private WeatherModel weatherModel;
    private String applicationName;
    private String buildVersion;
    private final IPConnection ipcon;
    private boolean nightmode;

    
    @Autowired
    public WeatherStation(@Value("${info.app.name}") String applicationName,
            @Value("${info.app.version}") String buildVersion, @Value("${weatherstation.mode.nightmode}") boolean nightmode) throws Exception {
        this.applicationName = applicationName;
        this.buildVersion = buildVersion;
        this.nightmode = nightmode;
        
        weatherModel = new WeatherModel();

        ipcon = new IPConnection();
        ipcon.connect(HOST, PORT);
        ipcon.addEnumerateListener(this);
        // configure all bricks and bricklets
        ipcon.enumerate();

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
        ipcon.disconnect();
    }


    @Override
    public void enumerate(String uid, String connectedUid, char position, short[] hardwareVersion,
            short[] firmwareVersion, int deviceIdentifier, short enumerationType) {
        switch (deviceIdentifier) {
        case 13:
            String UIDmaster = uid;
            BrickMaster master = new BrickMaster(UIDmaster, ipcon);
            new TempPoller(master);
            try {
                master.disableStatusLED();
            } catch (TimeoutException | NotConnectedException ex) {
                log.error(ex.getLocalizedMessage());
            }
            break;
        case 17:
            String UIDred = uid;
            break;
        case 21:
            String UIDamb = uid;
            final BrickletAmbientLight ambientLightBrick = new BrickletAmbientLight(UIDamb, ipcon);
            try {
                ambientLightBrick.setIlluminanceCallbackPeriod(callbackPeriodMsec);
            } catch (Exception ex) {
                log.error(ex.getLocalizedMessage());
            }
            ambientLightBrick.addIlluminanceListener(new IlluminanceListenerX(weatherModel));
            break;
        case 212:
            String UIDlcd = uid;
            BrickletLCD20x4 lcd = new BrickletLCD20x4(UIDlcd, ipcon);
            try {
                lcd.backlightOff();
                lcd.clearDisplay();
                lcd.backlightOn();
                lcd.writeLine((short) 0, (short) 0, applicationName);
                lcd.writeLine((short) 1, (short) 0, buildVersion);
                WeatherViewLcd24x4 lcdView = new WeatherViewLcd24x4(weatherModel, lcd);
                lcdView.setNightMode(nightmode);
            } catch (TimeoutException | NotConnectedException ex) {
                log.error(ex.getLocalizedMessage());
            }

            break;
        case 221:
            String UIDbar = uid;
            final BrickletBarometer barBrick = new BrickletBarometer(UIDbar, ipcon);
            try {
                barBrick.setAirPressureCallbackPeriod(callbackPeriodMsec);
            } catch (Exception ex) {
                log.error(ex.getLocalizedMessage());
            }
            barBrick.addAirPressureListener(new AirPressureListenerX(weatherModel, barBrick));
            break;
        case 27:
            String UIDhum = uid;
            final BrickletHumidity humBrick = new BrickletHumidity(UIDhum, ipcon);
            final HumidityListenerX humListener = new HumidityListenerX(weatherModel);
            try {
                humBrick.setHumidityCallbackPeriod(callbackPeriodMsec);
            } catch (Exception ex) {
                log.error(ex.getLocalizedMessage());
            }
            humBrick.addHumidityListener(humListener);
            break;
        default:
            log.warn("Unsupported Brick or Bricklet. Uid: "+uid + "  " +connectedUid+ " "+ position+ " "+hardwareVersion+" "+firmwareVersion+ " "+ deviceIdentifier+ " "+ enumerationType);
            //              2GewbC       0                   0           [S@150d707          [S@197219d                      17                    1
            break;
        }
    }
}