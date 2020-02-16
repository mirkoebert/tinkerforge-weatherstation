package com.mirkoebert.weather;

import lombok.Getter;

public final class AirpressurePoint {
    
    final long date;
    @Getter
    final double airpressureQFE;
    
    public AirpressurePoint(double airpressureQFE) {
        if (airpressureQFE < 1) {
            throw new IllegalArgumentException("Airperssure should always positiv. Given: " + airpressureQFE);
        }
        this.date = System.currentTimeMillis();
        this.airpressureQFE = airpressureQFE;
    }

}
