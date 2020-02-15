package com.mirkoebert.weather;

import lombok.Getter;

@Getter
public final class AirpressurePoint {
    

    final long date;
    final double airpressureQFE;
    
    public AirpressurePoint(long date, double airpressureQFE) {
        if (airpressureQFE < 1) {
            throw new IllegalArgumentException("Airperssure should alway positiv. Given: " + airpressureQFE);
        }
        this.date = date;
        this.airpressureQFE = airpressureQFE;
    }

}
