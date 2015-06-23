package com.ebertp;

import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public final class WarningView implements Runnable {


	private BrickletLCD20x4 lcd;
	private HumidityListenerX humx;

	public WarningView(BrickletLCD20x4 lcd, HumidityListenerX humx) {
		this.lcd=lcd;
		this.humx=humx;
	}

	@Override
	public void run() {
		while(true){
			try {
				lcd.writeLine((short)3, (short)0, humx.getMessage() );
			} catch (TimeoutException | NotConnectedException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}



}
