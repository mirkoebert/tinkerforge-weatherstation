package com.mirkoebert.weather.tinkerforge;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Monitor the weather data (weather model) and predict weather.
 * 
 * @author mirkoebert
 *
 */
@Component
@RequiredArgsConstructor
public class TinkerforgeWeatherMonitor {

    static final String NO_FORECAST = "Keine Vorhersage möglich";
    private final TinkerforgeWeather tinkerforgeWeather;
    private final AirPressurePointRepository repo;

    @Getter
    private boolean alarm;

    private boolean isHumidityAlarm() {
        final double h = tinkerforgeWeather.getHumdidity();
        return h > 0 && (h > 60 || h < 40);
    }

    private boolean isFrostAlarm() {
        final double t = tinkerforgeWeather.getTempIn();
        return t < 5 && t > -273;
    }

    private boolean isFireAlarm() {
        return tinkerforgeWeather.getTempIn() > 50;
    }

    /**
     * Predict storm based on {@link http://www.bohlken.net/luftdruck2.htm}
     * 
     * @return true if storm is predicted.
     */
    private boolean isStormAlarm() {
        boolean r = false;
        if (repo.getSize() > 5) {
            final AirpressurePoint min = repo.getMin();
            final AirpressurePoint max = repo.getMax();
            final double deltaMaxMin = max.airpressureQFE - min.airpressureQFE;
            if (deltaMaxMin > 3.3) {
                r = true;
            } else if (deltaMaxMin > 2 && max.date < min.date) {
                r = true;
            }
        }
        return r;
    }

    /**
     * Weather forecast.
     * 
     * @return Short forecast string to display on weather station's LSD.
     */
    public String getMessage() {
        String r = "";
        alarm = true;
        if (isHumidityAlarm()) {
            r = "Warnung: Luftfeuchtigkeit";
        } else if (isFrostAlarm()) {
            r = "Warnung: Frostgefahr";
        } else if (isStormAlarm()) {
            r = "Warnung: Sturm";
        } else if (isFireAlarm()) {
            r = "Warnung: Feuer";
        } else {
            r = getForeCast();
            alarm = false;
        }
        return r;
    }

    public String getAirpPressureTrend() {
        AirPressureTrend trend = getAirPressureTrend();
        if (trend == AirPressureTrend.falling) {
            return "fallend";
        } else if (trend == AirPressureTrend.stable) {
            return "gleichbleibend";
        } else if (trend == AirPressureTrend.unknown) {
            return "unknown";
        } else {
            return "steigend";
        }
    }

    /**
     * 
     * @return Weather forecast for the next 1-2 hours.
     */
    public String getForeCast() {
        String r = "Wetter unverändert";
        final double ap = tinkerforgeWeather.getAirPressureQFE();
        if (ap > 1020) {
            r = "Gutes Wetter";
        } else if (ap == -1) {
            r = NO_FORECAST;
        } else if (ap < 950) {
            r = "Unfreundliches Wetter";
        }

        if (repo.getSize() > 5) {
            final AirpressurePoint min = repo.getMin();
            final AirpressurePoint max = repo.getMax();
            final double delta = max.airpressureQFE - min.airpressureQFE;
            if (isAirpressureDecreasing(min, max)) {
                if (delta > 1) {
                    r = "Starker Wind 6-7 Bft";
                    if (delta > 2) {
                        r = "Wind 8-12 Bft";
                    }
                }
            } else {
                if (delta > 1.3 && delta < 2) {
                    r = "Starker Wind 6-7 Bft";
                } else if (delta >= 2 && delta < 3) {
                    r = "Wind 8-9 Bft";
                } else if (delta > 3) {
                    r = "Sturm 10- Bft";
                }
            }
        }
        return r;
    }

    private boolean isAirpressureDecreasing(final AirpressurePoint min, final AirpressurePoint max) {
        return max.date < min.date;
    }

    private AirPressureTrend getAirPressureTrend() {
        if (repo.getSize() < 5) {
            return AirPressureTrend.unknown;
        }
        final AirpressurePoint min = repo.getMin();
        final AirpressurePoint max = repo.getMax();
        final double delta = max.airpressureQFE - min.airpressureQFE;
        if (Math.abs(delta) < 0.6) {
            return AirPressureTrend.stable;
        } else if (isAirpressureDecreasing(min, max)) {
            return AirPressureTrend.falling;
        } else {
            return AirPressureTrend.rising;
        }
    }

}
