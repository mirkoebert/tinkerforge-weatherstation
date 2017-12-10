package com.ebertp;

import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public final class WeatherStation {

	private static final String HOST = "localhost";
	private static final int PORT = 4223;
	private static final String UIDamb = "mhH";
	private static final String UIDbar = "k6y";
	// private static final String UIDmas = "6Ka5bE";
	private static final String UIDhum = "nBj";
	private static final String UIDlcd = "odC";

	final BrickletLCD20x4 lcd;
	
	public static void main(final String[] args) throws Exception {
		new WeatherStation();
	}

	private final IPConnection ipcon;

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
		lcd.writeLine((short)0, (short)0, " Weather Station" );
		lcd.backlightOn();
		
		WeatherModel m = new WeatherModel();
		
		
		final HumidityListenerX humListener = new HumidityListenerX(m);

		humBrick.setHumidityCallbackPeriod(30003);
		humBrick.addHumidityListener(humListener);
		
		barBrick.setAirPressureCallbackPeriod(30002);
		barBrick.addAirPressureListener(new AirPressureListenerX(m, barBrick));
		
		ambientLightBrick.setIlluminanceCallbackPeriod(30001);
		ambientLightBrick.addIlluminanceListener(new IlluminanceListenerX(m));

		WeatherMonitor monitor = new WeatherMonitor(m);
		
		WeatherViewLcd24x4 v = new WeatherViewLcd24x4(m, lcd, monitor);
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