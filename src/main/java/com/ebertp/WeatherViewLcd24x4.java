package com.ebertp;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public class WeatherViewLcd24x4 implements Runnable{

	WeatherModel m;
	final BrickletLCD20x4 lcd;
	private final DecimalFormat df1 = new DecimalFormat("#.0");
	private final DecimalFormat df0 = new DecimalFormat("#");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("d. MMMM");
	private SimpleDateFormat sdf2 = new SimpleDateFormat("EE HH:mm:ss");
	private boolean timeOrdate = true;
	private WeatherMonitor monitor;


	public WeatherViewLcd24x4(WeatherModel m, BrickletLCD20x4 lcd, WeatherMonitor monitor) {
		this.m=m;
		this.lcd = lcd;
		this.monitor = monitor;
		final Thread t = new Thread(this);
		t.start();
	}


	public void paint() {
		try {
			lcd.clearDisplay();
			lcd.backlightOn();
			lcd.writeLine((short) 0, (short) 12, (int) Math.round(m.getAirPressure()) + " hPa");
			lcd.writeLine((short) 0, (short) 0 , df1.format(m.getHumdidity()) + " %RH");
			lcd.writeLine((short) 1, (short) 0 , df1.format(m.getTempIn())+" C" );
			lcd.writeLine((short) 2, (short) 0,  df0.format(m.getIllumination()) + " lx  ");

			Date d= new Date();
			String message;
			if (timeOrdate){
				message = sdf1.format(d);
			} else {
				message = sdf2.format(d);
			}
			lcd.writeLine((short)1, (short)8, message );
			
			if (monitor.isHumidityAlarm()) {
				lcd.writeLine((short) 3, (short) 0,  "Warnung: Luftfeuchtigkeit");
			} else if (monitor.isFrostAlarm()) {
				lcd.writeLine((short) 3, (short) 0,  "Warnung: Frostgefahr");
			} else if (monitor.isStormAlarm()) {
				lcd.writeLine((short) 3, (short) 0,  "Warnung: Sturm");
			}
			timeOrdate = !timeOrdate;

		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			paint();
		}


	}
}
