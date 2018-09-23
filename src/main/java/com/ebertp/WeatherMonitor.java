package com.ebertp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WeatherMonitor {

	private WeatherModel m;
	private boolean alarm = false;
	private List<AirpressurePoint> aplist = new ArrayList<AirpressurePoint>();
	final long h1inMsec = 60 * 60 * 1000;

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
		return (m.getTempIn() > 50);
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
		if ((max.airpressure - min.airpressure) > 3.3) {
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
		
		for (Iterator<AirpressurePoint> iterator = aplist.iterator(); iterator.hasNext();) {
			AirpressurePoint airpressurePoint = iterator.next();
			if( (now - airpressurePoint.date) > h1inMsec) {
				iterator.remove();
			}
		}
	}

	public String getForeCast() {
		String r = "Wetter unverändert";
		double ap = m.getAirPressure();
		if(ap >1020) {
			r = r + " gut";
		}
		long d = m.getDate();
		AirpressurePoint cap = new AirpressurePoint(d,ap);


		AirpressurePoint min = getMin(cap);
		AirpressurePoint max = getMax(cap);
		double delta = max.airpressure - min.airpressure;
		if (isFallend(min, max)) {
			if (delta > 1) { 
				r="Wind 6-7 Bft";
				if (delta > 2) {
					r="Wind 8-12 Bft";
				}
			}
		} else {
			if((delta > 1.3)&&(delta < 2)) {
				r = "Wind 6-7 Bft";
			} else if((delta >=2)&& (delta < 3)) {
				r = "Wind 8-9 Bft";
			} else if (delta > 3) {
				r = "Wind 10- Bft";
			}
		}
		return r;
	}

	private boolean isFallend(AirpressurePoint min, AirpressurePoint max) {
		return max.date < min.date;
	}


}
