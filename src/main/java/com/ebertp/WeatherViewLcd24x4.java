package com.ebertp;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.boot.SpringApplication;

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
	


	public WeatherViewLcd24x4(WeatherModel m, BrickletLCD20x4 lcd) {
		this.m=m;
		this.lcd = lcd;
		final Thread t = new Thread(this);
		t.start();
	}


	public void paint() {
		try {
			lcd.clearDisplay();
			switchBacklightOffAtNight();

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

			
			lcd.writeLine((short) 3, (short) 0,  m.getForecast());
			timeOrdate = !timeOrdate;

		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}
	}


	private void switchBacklightOffAtNight() {
		int h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		try {
			if ((h > 5)&&(h < 22)) {
				lcd.backlightOn();
			} else {
				lcd.backlightOff();
			}
		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(3500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			paint();
		}
	}

	
}
