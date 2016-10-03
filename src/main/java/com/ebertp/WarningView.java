package com.ebertp;

import java.util.Vector;

import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public final class WarningView implements Runnable {

	private String r = "                                ";

	private Vector<SensorWithWarningsInterface> sensorlist = new Vector<SensorWithWarningsInterface>(); 
	private BrickletLCD20x4 lcd;

	public WarningView(BrickletLCD20x4 lcd) {
		this.lcd=lcd;
	}

	public void addWarningSensor(SensorWithWarningsInterface s){
		sensorlist.addElement(s);
	}
	
	private boolean hasWarnings(){
		boolean displayWarning = false;
		for (SensorWithWarningsInterface sensorWithWarnings : sensorlist) {
			String msg = sensorWithWarnings.getWarningMessage();
			if(!msg.isEmpty()){
				displayWarning=true;
			}
		}
		return displayWarning;
	}
	
	private Vector<String> getAllWarningMessages(){
		Vector<String> r = new Vector<String>();
		for (SensorWithWarningsInterface sensorWithWarnings : sensorlist) {
			String msg = sensorWithWarnings.getWarningMessage();
			if(!msg.isEmpty()){
				r.add(msg);
				
			}
		}
		return r;
	}
	
	@Override
	public void run() {
		while(true){
			try {
				String msg = r;
				if(hasWarnings()){
					Vector<String> allmsg = getAllWarningMessages(); 
					msg = allmsg.lastElement();
					System.out.println("Warning: "+msg);
				}
				lcd.writeLine((short)3, (short)0, msg );

			} catch (TimeoutException | NotConnectedException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}



}
