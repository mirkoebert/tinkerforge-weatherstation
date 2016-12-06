package com.ebertp;

import java.util.Vector;

import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public final class WarningView implements Runnable {

	private final BrickletLCD20x4 lcd;

	private final String r = "                                ";
	private final Vector<SensorWithWarningsInterface> sensorlist = new Vector<>();

	public WarningView(final BrickletLCD20x4 lcd) {
		this.lcd = lcd;
	}

	public void addWarningSensor(final SensorWithWarningsInterface s) {
		sensorlist.addElement(s);
	}

	private Vector<String> getAllWarningMessages() {
		final Vector<String> r = new Vector<>();
		for (final SensorWithWarningsInterface sensorWithWarnings : sensorlist) {
			final String msg = sensorWithWarnings.getWarningMessage();
			if (!msg.isEmpty()) {
				r.add(msg);

			}
		}
		return r;
	}

	private boolean hasWarnings() {
		boolean displayWarning = false;
		for (final SensorWithWarningsInterface sensorWithWarnings : sensorlist) {
			final String msg = sensorWithWarnings.getWarningMessage();
			if (!msg.isEmpty()) {
				displayWarning = true;
			}
		}
		return displayWarning;
	}

	@Override
	public void run() {
		while (true) {
			try {
				String msg = r;
				if (hasWarnings()) {
					final Vector<String> allmsg = getAllWarningMessages();
					msg = allmsg.lastElement();
					System.out.println("Warning: " + msg);
				}
				lcd.writeLine((short) 3, (short) 0, msg);

			} catch (TimeoutException | NotConnectedException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(1500);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
