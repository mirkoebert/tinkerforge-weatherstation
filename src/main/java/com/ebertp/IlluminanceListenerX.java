package com.ebertp;

import java.text.DecimalFormat;

import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.BrickletAmbientLight.IlluminanceListener;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

final class IlluminanceListenerX implements IlluminanceListener {
	
	private BrickletLCD20x4 lcd;
	private DecimalFormat df = new DecimalFormat("#");

	public IlluminanceListenerX(BrickletLCD20x4 lcd){
		this.lcd=lcd;
	}
	
	@Override
	public void illuminance(int illuminance) {
		long ill = Math.round(illuminance/10.0);
		System.out.println("Illuminance: "+ill);
		try {
			if(ill>500){
				lcd.backlightOff();
			} else {
				lcd.backlightOn();
			}			
			lcd.writeLine((short)2, (short)0, df.format(ill) + " lx  ");
		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}

	}

}
