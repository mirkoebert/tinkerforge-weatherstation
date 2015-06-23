package com.ebertp;

import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;
import com.tinkerforge.BrickletBarometer.AirPressureListener;

final class AirPressureListenerX implements AirPressureListener {

	private BrickletLCD20x4 lcd;
	private BrickletBarometer barometer;

	public AirPressureListenerX(BrickletLCD20x4 lcd,BrickletBarometer barometer) {
		this.lcd=lcd;
		this.barometer=barometer;
	}

	@Override
	public void airPressure(int airPressure) {
        System.out.println("Luftdruck: " + airPressure/1000 + " mBar");
        try {
        	float QFE = airPressure/1000f;
        	double Tg = 0.0065;
        	double H = 23;
        	double Tfe = barometer.getChipTemperature()/100.0;
        	System.out.println("Tfe: "+Tfe);
        	double QFF = QFE / Math.pow((1 - Tg * H / (273.15 + Tfe + Tg * H)) , (0.034163 / Tg));
        	int qff = (int) Math.round(QFF);
			lcd.writeLine((short)0, (short)12, qff + " hPa");
		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}


	}

}
