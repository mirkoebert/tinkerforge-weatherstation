package com.ebertp;

import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public final class WeatherStation {

  @Value("${application.name}")
  private String applicationName;

  @Value("${application.version}")
  private String buildVersion;

	private static final String HOST = "localhost";
	private static final int PORT = 4223;
	
  // TODO get UIDs from station
	private static final String UIDamb = "mhH";
	private static final String UIDbar = "k6y";
	// private static final String UIDmas = "6Ka5bE";
	private static final String UIDhum = "nBj";
	private static final String UIDlcd = "odC";

	private final BrickletLCD20x4 lcd;
	@Getter 	private WeatherModel weatherModel;
	
	private final IPConnection ipcon;	
	
	@Autowired
	public WeatherStation(@Value("${application.name}") String applicationName,@Value("${application.version}") String buildVersion) throws Exception {
		ipcon = new IPConnection();

		final BrickletHumidity humBrick = new BrickletHumidity(UIDhum, ipcon);
		lcd = new BrickletLCD20x4(UIDlcd, ipcon);
		final BrickletBarometer barBrick = new BrickletBarometer(UIDbar, ipcon);
		final BrickletAmbientLight ambientLightBrick = new BrickletAmbientLight(UIDamb, ipcon);

		ipcon.connect(HOST, PORT);
		// start message
		lcd.backlightOff();
		lcd.clearDisplay();
    lcd.backlightOn();
		lcd.writeLine((short)0, (short)0, applicationName );
    lcd.writeLine((short)0, (short)0, buildVersion );
		log.info(applicationName+ " "+buildVersion);

		
		weatherModel = new WeatherModel();
		
		final HumidityListenerX humListener = new HumidityListenerX(weatherModel);

		humBrick.setHumidityCallbackPeriod(30003);
		humBrick.addHumidityListener(humListener);
		
		barBrick.setAirPressureCallbackPeriod(30002);
		barBrick.addAirPressureListener(new AirPressureListenerX(weatherModel, barBrick));
		
		ambientLightBrick.setIlluminanceCallbackPeriod(30001);
		ambientLightBrick.addIlluminanceListener(new IlluminanceListenerX(weatherModel));

		WeatherMonitor monitor = new WeatherMonitor(weatherModel);
		
		WeatherViewLcd24x4 v = new WeatherViewLcd24x4(weatherModel, lcd);
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