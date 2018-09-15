package com.ebertp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author mirkoebert
 *
 */
public class WeatherAnalyzer {

    private WeatherModel weatherModel;
    private boolean alarm = false;
    private List<AirpressurePoint> aplist = new ArrayList<AirpressurePoint>();

    public WeatherAnalyzer(WeatherModel weatherModel) {
        this.weatherModel = weatherModel;
    }

    boolean isHumidityAlarm() {
        double h = weatherModel.getHumdidity();
        alarm = (h > 60 || h < 40);
        return alarm;
    }

    public boolean isFrostAlarm() {
        return (weatherModel.getTempIn() < 5);
    }

    public boolean isFireAlarm() {
        return (weatherModel.getTempIn() > 50);
    }

    /** 
     * @link http://www.bohlken.net/luftdruck2.htm
     * @return
     */
    public boolean isStormAlarm() {
        removeOldData();
        boolean r = false;
        double ap = weatherModel.getAirPressure();
        long d = weatherModel.getDate();
        AirpressurePoint cap = new AirpressurePoint(d, ap);

        AirpressurePoint min = getMin(cap);
        AirpressurePoint max = getMax(cap);
        double deltaAirpressure = max.airpressure - min.airpressure;
        if (deltaAirpressure > 3.3) {
            r = true;
        } else if ((deltaAirpressure > 2) && (max.date < min.date)) {
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
        for (Iterator<AirpressurePoint> iterator = aplist.iterator(); iterator.hasNext();) {
            AirpressurePoint airpressurePoint = iterator.next();
            if ((now - airpressurePoint.date) > h1inMsec) {
                iterator.remove();
            }
        }
    }

    public String getForeCast() {
        String r = "unverändert";
        double ap = weatherModel.getAirPressure();
        if (ap > 1019) {
            r = r + " gut";
        }
        long d = weatherModel.getDate();
        AirpressurePoint cap = new AirpressurePoint(d, ap);

        AirpressurePoint min = getMin(cap);
        AirpressurePoint max = getMax(cap);
        double deltaAirpressure = max.airpressure - min.airpressure;
        if (isFallend(min, max)) {
            if (deltaAirpressure > 1) {
                r = "6-7 Bft";
                if (deltaAirpressure > 2) {
                    r = "8-12 Bft";
                }
            }
        } else {
            if ((deltaAirpressure > 1.3) && (deltaAirpressure < 2)) {
                r = "6-7 Bft";
            } else if ((deltaAirpressure >= 2) && (deltaAirpressure < 3)) {
                r = "8-9 Bft";
            } else if (deltaAirpressure > 3) {
                r = "10- Bft";
            }
        }
        return r;
    }

    private boolean isFallend(AirpressurePoint min, AirpressurePoint max) {
        return max.date < min.date;
    }

}
