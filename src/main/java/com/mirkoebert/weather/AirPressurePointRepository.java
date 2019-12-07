package com.mirkoebert.weather;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import lombok.Getter;

public class AirPressurePointRepository {

    @Getter
    private List<AirpressurePoint> aplist = new ArrayList<AirpressurePoint>();
    private static final long H1_IN_MSEC = 60 * 60 * 1000;

    @Getter
    private static final AirPressurePointRepository INSTANCE = new AirPressurePointRepository();
    
    private void removeOldData() {
        final long now = System.currentTimeMillis();
        aplist.removeIf((airpressurePoint) -> (now - airpressurePoint.date) > H1_IN_MSEC);
    }
    
    void add(AirpressurePoint cap) {
        removeOldData();
        aplist.add(cap);
    }

    public AirpressurePoint getMin() {
        Comparator<AirpressurePoint> comparator = Comparator.comparing( AirpressurePoint::getAirpressureQFE );
        return aplist.stream().min(comparator).get();
    }

    public AirpressurePoint getMax() {
        Comparator<AirpressurePoint> comparator = Comparator.comparing( AirpressurePoint::getAirpressureQFE );
        return aplist.stream().max(comparator).get();
    }

}
