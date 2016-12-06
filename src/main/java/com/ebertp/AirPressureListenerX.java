package com.ebertp;

import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletBarometer.AirPressureListener;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * Air pressure listener implementing a Tinkerforge air pressure listener
 * (callback). Return the air pressure correct by temperature (from chip) and
 * current high over sea level.
 *
 * @author mebert
 *
 */
final class AirPressureListenerX implements AirPressureListener {

	private final BrickletBarometer barometer;
	private final BrickletLCD20x4 lcd;

	/**
	 * Constructor.
	 *
	 * @param lcd
	 *            lcd bricklet 20 x 4
	 * @param barometer
	 */
	public AirPressureListenerX(final BrickletLCD20x4 lcd, final BrickletBarometer barometer) {
		this.lcd = lcd;
		this.barometer = barometer;
	}

	@Override
	public void airPressure(final int airPressure) {
		System.out.println("Air pressure: " + airPressure / 1000 + " mBar");
		try {
			final float QFE = airPressure / 1000f;
			final double Tg = 0.0065;
			final double H = 23;
			final double Tfe = barometer.getChipTemperature() / 100.0;
			System.out.println("Tfe: " + Tfe);
			final double QFF = QFE / Math.pow(1 - Tg * H / (273.15 + Tfe + Tg * H), 0.034163 / Tg);
			final int qff = (int) Math.round(QFF);
			lcd.writeLine((short) 0, (short) 12, qff + " hPa");
		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}

	}

}
