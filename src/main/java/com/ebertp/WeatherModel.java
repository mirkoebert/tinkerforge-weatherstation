package com.ebertp;

public class WeatherModel {

	double tempIn, tempOut;
	long illumination;
	long date;
	double airPressure, humdidity;

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public long getIllumination() {
		return illumination;
	}

	public void setIllumination(long illumination) {
		this.illumination = illumination;
	}

	public double getTempIn() {
		return tempIn;
	}

	public void setTempIn(double tempIn) {
		this.tempIn = tempIn;
	}

	public double getTempOut() {
		return tempOut;
	}

	public void setTempOut(float tempOut) {
		this.tempOut = tempOut;
	}

	public double getAirPressure() {
		date = System.currentTimeMillis();
		return airPressure;
	}

	public void setAirPressure(double airPressure) {
		this.airPressure = airPressure;
	}

	public double getHumdidity() {
		return humdidity;
	}

	public void setHumdidity(double humdidity) {
		this.humdidity = humdidity;
	}



}
