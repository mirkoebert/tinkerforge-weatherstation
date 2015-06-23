package com.ebertp;

import java.text.SimpleDateFormat;
import java.util.Date;


import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

final class DateSetter implements Runnable {

	private BrickletLCD20x4 lcd;
	private Date d;
	private SimpleDateFormat sdf1 = new SimpleDateFormat("d.MMMM yyyy");
	private SimpleDateFormat sdf2 = new SimpleDateFormat(" EE,HH:mm:ss");
	private boolean timeOrdate = true;

	public DateSetter(BrickletLCD20x4 lcd) {
		this.lcd=lcd;
	}

	@Override
	public void run() {
		String message;
		while(true){
			d = new Date();
			try {
				if (timeOrdate){
					message = sdf1.format(d);
				} else {
					message = sdf2.format(d);
				}
				lcd.writeLine((short)1, (short)8, message );
				timeOrdate = !timeOrdate;
			} catch (TimeoutException | NotConnectedException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


	}

}
