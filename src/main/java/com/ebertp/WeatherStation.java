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

		final BrickletHumidity hum = new BrickletHumidity(UIDhum, ipcon);
		lcd = new BrickletLCD20x4(UIDlcd, ipcon);
		final BrickletBarometer bar = new BrickletBarometer(UIDbar, ipcon);
		final BrickletAmbientLight ambientLight = new BrickletAmbientLight(UIDamb, ipcon);

		ipcon.connect(HOST, PORT);
		lcd.backlightOff();
		lcd.clearDisplay();
		lcd.writeLine((short)0, (short)0, "Weather Station" );
		lcd.backlightOn();

		
		
		final HumidityListenerX humx = new HumidityListenerX(lcd);
		final WarningView wv = new WarningView(lcd);
		wv.addWarningSensor(humx);

		hum.setHumidityCallbackPeriod(18001);
		hum.addHumidityListener(humx);
		
		bar.setAirPressureCallbackPeriod(19002);
		bar.addAirPressureListener(new AirPressureListenerX(lcd, bar));
		
		ambientLight.setIlluminanceCallbackPeriod(23001);
		ambientLight.addIlluminanceListener(new IlluminanceListenerX(lcd));

		final DateSetter ds = new DateSetter(lcd);
		final Thread t = new Thread(ds);
		t.start();
		final Thread t2 = new Thread(wv);
		t2.start();
		final TemperatureHelper th = new TemperatureHelper(lcd, bar);
		wv.addWarningSensor(th);
		final Thread t3 = new Thread(th);
		t3.start();
		
		lcd.clearDisplay();
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