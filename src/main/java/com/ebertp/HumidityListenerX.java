package com.ebertp;

import java.text.DecimalFormat;

import com.tinkerforge.BrickletHumidity.HumidityListener;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * Humidity listener that also calculates warning if the relative humidity is
 * out of human comfort zone. The Warning is displayed on the LCD display.
 *
 * @author mebert
 *
 */
class HumidityListenerX implements HumidityListener, SensorWithWarningsInterface {

	private final DecimalFormat df = new DecimalFormat("#.0");
	private long lastWarningOccurance = 0;
	private final BrickletLCD20x4 lcd;
	private final String warningMessage = "Warn: Humidity";

	public HumidityListenerX(final BrickletLCD20x4 lcd) {
		this.lcd = lcd;
	}

	@Override
	public String getWarningMessage() {
		String r = "";
		if (System.currentTimeMillis() - lastWarningOccurance < 4000) {
			r = warningMessage;
		}
		return r;
	}

	@Override
	public void humidity(final int humidity) {
		final double h = humidity / 10.0;
		System.out.println("Relative Humidity: " + h + " %RH");
		try {
			lcd.writeLine((short) 0, (short) 0, df.format(h) + " %RH");
			if (h > 60 || h < 40) {
				System.out.println("Warn: Humidity: " + h + " %RH");
				lastWarningOccurance = System.currentTimeMillis();
			}
		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}
	}

}