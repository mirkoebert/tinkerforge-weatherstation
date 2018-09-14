package com.ebertp;

import com.tinkerforge.BrickletAmbientLight.IlluminanceListener;

/**
 * Illumination listener that also switches the back light of the LCD display.
 * 
 * @author mebert
 *
 */
final class IlluminanceListenerX implements IlluminanceListener {

    private WeatherModel m;

    public IlluminanceListenerX(WeatherModel m) {
        this.m = m;
    }

    @Override
    public void illuminance(final int illuminance) {
        final long ill = Math.round(illuminance / 10.0);
        m.setIllumination(ill);
        System.out.println("Illuminance: " + ill);
//		try {
//			if (ill > 400) {
//				lcd.backlightOff();
//			} else {
//				int h = Calendar.HOUR_OF_DAY;
//				if ((h > 6)&&(h < 21)) {
//					lcd.backlightOn();
//				} else {
//					//System.out.println("Nigh tmode");
//					lcd.backlightOff();
//				}
//			}
//			lcd.writeLine((short) 2, (short) 0, df.format(ill) + " lx  ");
//		} catch (TimeoutException | NotConnectedException e) {
//			e.printStackTrace();
//		}

    }

}
