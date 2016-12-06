package com.ebertp;

import java.text.DecimalFormat;

import com.tinkerforge.BrickletAmbientLight.IlluminanceListener;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * Illumination listener that also switches the back light of the LCD display.
 * 
 * @author mebert
 *
 */
final class IlluminanceListenerX implements IlluminanceListener {

	private final DecimalFormat df = new DecimalFormat("#");
	private final BrickletLCD20x4 lcd;

	public IlluminanceListenerX(final BrickletLCD20x4 lcd) {
		this.lcd = lcd;
	}

	@Override
	public void illuminance(final int illuminance) {
		final long ill = Math.round(illuminance / 10.0);
		System.out.println("Illuminance: " + ill);
		try {
			if (ill > 500) {
				lcd.backlightOff();
			} else {
				lcd.backlightOn();
			}
			lcd.writeLine((short) 2, (short) 0, df.format(ill) + " lx  ");
		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}

	}

}
