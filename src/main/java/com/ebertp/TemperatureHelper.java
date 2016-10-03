package com.ebertp;

import java.text.DecimalFormat;

import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public final class TemperatureHelper implements Runnable, SensorWithWarningsInterface {

		private BrickletLCD20x4 lcd;
		private BrickletBarometer barometer;
		DecimalFormat df = new DecimalFormat("#.0");
		private long lastWarningOccurance = 0;
		private String warningMessage = "Warn: Frost damage risk";		

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
					if(rf<5){
						System.out.println("Warn: Temp: " + rf + " C");
						lastWarningOccurance = System.currentTimeMillis();
					}
								
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

		@Override
		public String getWarningMessage() {
			String r = "";
			if((System.currentTimeMillis() - lastWarningOccurance) < 2000){
				r = warningMessage;
			}
			return r;
		}

	}
