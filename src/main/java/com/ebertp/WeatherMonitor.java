package com.ebertp;

import java.util.ArrayList;
import java.util.List;

public class WeatherMonitor {

	private WeatherModel m;
	private boolean alarm = false;
	private List<AirpressurePoint> aplist = new ArrayList<AirpressurePoint>();
	

	public  WeatherMonitor(WeatherModel m) {
		this.m = m;
	}

	boolean isHumidityAlarm(){
		double h = m.getHumdidity();
		alarm  = (h > 60 || h < 40);
		return alarm;
	}

	public boolean isFrostAlarm() {
		return (m.getTempIn() < 5);
	}
	
	public boolean isFireAlarm() {
		return (m.getTempIn() > 55);
	}

	/**
	 * @link http://www.bohlken.net/luftdruck2.htm
	 * @return
	 */
	public boolean isStormAlarm() {
		removeOldData();
		boolean r = false;
		double ap = m.getAirPressure();
		long d = m.getDate();
		AirpressurePoint cap = new AirpressurePoint(d,ap);
		
		
		AirpressurePoint min = getMin(cap);
		AirpressurePoint max = getMax(cap);
		if ((max.airpressure -min.airpressure) > 3.3) {
			r = true;
		} else if(((max.airpressure -min.airpressure) > 2) && (max.date < min.date) ) {
			r = true;
		}
		aplist.add(cap);
		return r;
	}

	private AirpressurePoint getMin(AirpressurePoint current) {
		AirpressurePoint min = current;
		for (AirpressurePoint airpressurePoint : aplist) {
			if (airpressurePoint.airpressure < min.airpressure) {
				min = airpressurePoint;
			}
		}
		return min;
	}

	private AirpressurePoint getMax(AirpressurePoint current) {
		AirpressurePoint max = current;
		for (AirpressurePoint airpressurePoint : aplist) {
			if (airpressurePoint.airpressure > max.airpressure) {
				max = airpressurePoint;
			}
		}
		return max;
	}
	
	private void removeOldData() {
		long now = System.currentTimeMillis();
		final long h1inMsec = 60 * 60 * 1000;
		for (AirpressurePoint airpressurePoint : aplist) {
			if( (now - airpressurePoint.date) > h1inMsec) {
				aplist.remove(airpressurePoint);
			}
		}
	}
	

}
