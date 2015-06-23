package com.ebertp;

import java.text.DecimalFormat;

import com.tinkerforge.BrickletHumidity.HumidityListener;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

class HumidityListenerX implements HumidityListener {
	
	private BrickletLCD20x4 lcd;
	private String warningMessage = "Warn: Humidity";
	private long lastWarningOccurance = 0;
	private DecimalFormat df = new DecimalFormat("#.0");

	public HumidityListenerX(BrickletLCD20x4 lcd) {
		this.lcd=lcd;
	}

	public void humidity(int humidity) {
        double h = humidity/10.0;
		System.out.println("Relative Humidity: " + h + " %RH");
        try {
			lcd.writeLine((short)0, (short)0, df.format(h) + " %RH");
			if((h>60)||(h<40)){
				System.out.println("Warn: Humidity: " + h + " %RH");
				lastWarningOccurance = System.currentTimeMillis();
			}
		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}
    }
	
	public String getMessage(){
		String r = "                                ";
		if((System.currentTimeMillis() - lastWarningOccurance) < 2000){
			r = warningMessage;
		}
		return r;
		
	}
    
}