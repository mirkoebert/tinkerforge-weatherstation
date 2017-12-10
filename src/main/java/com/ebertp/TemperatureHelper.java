package com.ebertp;


import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public final class TemperatureHelper implements Runnable, SensorWithWarningsInterface {

		private BrickletBarometer barometer;
		private long lastWarningOccurance = 0;
		private String warningMessage = "Warn: Frost damage risk";
		private WeatherModel m;		

		public TemperatureHelper(WeatherModel m, BrickletBarometer barometer) {
			this.m = m;
			this.barometer=barometer;
			final Thread t3 = new Thread(this);
			t3.start();
		}

		@Override
		public void run() {
//			String message = "";
			while(true){
				
				try {
					short raw = barometer.getChipTemperature();
					System.out.println("Temp: "+raw);
					float rf = raw/100.f;
					m.setTempIn(rf);
					if(rf<4){
						System.err.println("Warn: Temp: " + rf + " C");
						lastWarningOccurance = System.currentTimeMillis();
					}
								
				} catch (TimeoutException | NotConnectedException e) {
					e.printStackTrace();
				}
				// need this sleep, because this thread is not a call back listener
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}


		}

		@Override
		public String getWarningMessage() {
			String r = "";
			if((System.currentTimeMillis() - lastWarningOccurance) < 30000){
				r = warningMessage;
			}
			return r;
		}

	}
