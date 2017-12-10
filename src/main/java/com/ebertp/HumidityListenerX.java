package com.ebertp;


import com.tinkerforge.BrickletHumidity.HumidityListener;

/**
 * Humidity listener that also calculates warning if the relative humidity is
 * out of human comfort zone. The Warning is displayed on the LCD display.
 *
 * @author mebert
 *
 */
class HumidityListenerX implements HumidityListener, SensorWithWarningsInterface {

	private long lastWarningOccurance = 0;
	private final String warningMessage = "Warn: Humidity";
	private WeatherModel m;

	public HumidityListenerX(final WeatherModel m) {
		this.m = m;
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
		m.setHumdidity(h);
		System.out.println("Relative Humidity: " + h + " %RH");
//		try {
			//lcd.writeLine((short) 0, (short) 0, df.format(h) + " %RH");
			if (h > 60 || h < 40) {
				System.out.println("Warn: Humidity: " + h + " %RH");
				lastWarningOccurance = System.currentTimeMillis();
			}
//		} catch (TimeoutException | NotConnectedException e) {
//			e.printStackTrace();
//		}
	}

}