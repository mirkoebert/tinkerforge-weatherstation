package com.ebertp;

import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.BrickletHumidity.HumidityListener;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.IPConnection;

public final class WeatherStation {
    private static final String HOST = "localhost";
    private static final int PORT = 4223;
    //private static final String UIDmas = "6Ka5bE";
    private static final String UIDhum = "nBj";
    private static final String UIDlcd = "odC";
    private static final String UIDbar = "k6y";
    private static final String UIDamb = "mhH";
    private IPConnection ipcon = new IPConnection();
    
    public WeatherStation() throws Exception{
   	  
     BrickletHumidity  hum = new BrickletHumidity(UIDhum, ipcon);
     BrickletLCD20x4   lcd = new BrickletLCD20x4(UIDlcd, ipcon);
     BrickletBarometer bar = new BrickletBarometer(UIDbar, ipcon);
     BrickletAmbientLight ambientLight = new BrickletAmbientLight(UIDamb, ipcon);
     
     
     HumidityListenerX humx = new HumidityListenerX(lcd);
     WarningView wv = new WarningView(lcd,humx);
     
     ipcon.connect(HOST, PORT);
     lcd.backlightOff();
     lcd.clearDisplay();
     hum.setHumidityCallbackPeriod(4011);
     hum.addHumidityListener((HumidityListener) humx);
     bar.setAirPressureCallbackPeriod(15001);
     bar.addAirPressureListener(new AirPressureListenerX(lcd,bar));
     ambientLight.setIlluminanceCallbackPeriod(1301);
     ambientLight.addIlluminanceListener(new IlluminanceListenerX(lcd));

     
     DateSetter ds = new DateSetter(lcd);
     Thread t = new Thread(ds);
     t.start();
     lcd.backlightOn();
     Thread t2 = new Thread(wv);
     t2.start();
     TemperatureHelper th = new TemperatureHelper(lcd, bar);
     Thread t3 = new Thread(th);
     t3.start();

     
     Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
         public void run() {
        	 System.out.println("XXX");
         }	}));
    }
    
    @Override
    protected void finalize() throws Throwable {
    	super.finalize();
    	System.out.println("YYY");
    	ipcon.disconnect();
    }

    public static void main(String args[]) throws Exception {
    	new WeatherStation();
    }
}