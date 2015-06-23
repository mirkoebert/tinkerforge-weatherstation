package com.ebertp;

import java.text.DecimalFormat;

import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public final class TemperatureHelper implements Runnable {

		private BrickletLCD20x4 lcd;
		private BrickletBarometer barometer;
		DecimalFormat df = new DecimalFormat("#.0");
		

		public TemperatureHelper(BrickletLCD20x4 lcd, BrickletBarometer barometer) {
			this.lcd=lcd;
			this.barometer=barometer;
		}

		@Override
		public void run() {
			String message = "";
			while(true){
				
				try {
					short raw = barometer.getChipTemperature();
					System.out.println("Temp: "+raw);
					float rf = raw/100.f;
					message = df.format(rf)+" C";
					lcd.writeLine((short)1, (short)0, message );
				} catch (TimeoutException | NotConnectedException e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(3700);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}


		}

	}
