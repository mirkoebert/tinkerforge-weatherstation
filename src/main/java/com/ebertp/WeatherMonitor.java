package com.ebertp;

/**
 * Monitor the weather data and predict weather.
 * @author mirkoebert
 *
 */
public class WeatherMonitor {

    private WeatherModel m;
    private boolean alarm = false;

    public WeatherMonitor(WeatherModel m) {
        this.m = m;
    }

    boolean isHumidityAlarm() {
        double h = m.getHumdidity();
        alarm = (h > 60 || h < 40);
        return alarm;
    }

    public boolean isFrostAlarm() {
        return (m.getTempIn() < 5);
    }

    public boolean isFireAlarm() {
        return (m.getTempIn() > 50);
    }

    /**
     * Predict storm based on {@link http://www.bohlken.net/luftdruck2.htm}
     * 
     * @return true if storm is predicted.
     */
    public boolean isStormAlarm() {
        boolean r = false;
        double ap = m.getAirPressureQFE();
        long d = m.getDate();
        AirpressurePoint cap = new AirpressurePoint(d, ap);

        AirPressurePointRepository repo = AirPressurePointRepository.getINSTANCE();
        AirpressurePoint min = repo.getMin(cap);
        AirpressurePoint max = repo.getMax(cap);
        if ((max.airpressureQFE - min.airpressureQFE) > 3.3) {
            r = true;
        } else if (((max.airpressureQFE - min.airpressureQFE) > 2) && (max.date < min.date)) {
            r = true;
        }
        repo.add(cap);
        return r;
    }



    /**
     * 
     * @return Weather forecast for the next 1-2 hours.
     */
    public String getForeCast() {
        String r = "Wetter unverändert";
        double ap = m.getAirPressureQFE();
        if (ap > 1020) {
            r = "Gutes Wetter";
        } else if (ap < 950) {
            r = "Unfreundliches Wetter";
        } 
        long d = m.getDate();
        AirpressurePoint cap = new AirpressurePoint(d, ap);

        AirPressurePointRepository repo = AirPressurePointRepository.getINSTANCE();
        AirpressurePoint min = repo.getMin(cap);
        AirpressurePoint max = repo.getMax(cap);
        double delta = max.airpressureQFE - min.airpressureQFE;
        if (isFallend(min, max)) {
            if (delta > 1) {
                r = "Starker Wind 6-7 Bft";
                if (delta > 2) {
                    r = "Wind 8-12 Bft";
                }
            }
        } else {
            if ((delta > 1.3) && (delta < 2)) {
                r = "Starker Wind 6-7 Bft";
            } else if ((delta >= 2) && (delta < 3)) {
                r = "Wind 8-9 Bft";
            } else if (delta > 3) {
                r = "Sturm 10- Bft";
            }
        }
        return r;
    }

    private boolean isFallend(AirpressurePoint min, AirpressurePoint max) {
        return max.date < min.date;
    }
    
    public AirPressureTrend getAirPressureTrend() {
        double ap = m.getAirPressureQFE();
        long d = m.getDate();
        AirpressurePoint cap = new AirpressurePoint(d, ap);

        AirPressurePointRepository repo = AirPressurePointRepository.getINSTANCE();
        AirpressurePoint min = repo.getMin(cap);
        AirpressurePoint max = repo.getMax(cap);
        double delta = max.airpressureQFE - min.airpressureQFE;
        // TODO check value
        if (Math.abs(delta) < 0.6) {
            return AirPressureTrend.stable;
        } else if (isFallend(min, max)) {
            return AirPressureTrend.falling;
        } else {
            return AirPressureTrend.rising;
        }
    }

}
