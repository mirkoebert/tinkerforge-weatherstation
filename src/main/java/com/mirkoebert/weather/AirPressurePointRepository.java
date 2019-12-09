package com.mirkoebert.weather;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Repository;

import lombok.Getter;

@Repository
@Order(10)
public class AirPressurePointRepository {

    @Getter
    private List<AirpressurePoint> aplist = new ArrayList<AirpressurePoint>();
    private static final long H1_IN_MSEC = 60 * 60 * 1000;
    private Comparator<AirpressurePoint> comparator = Comparator.comparing( AirpressurePoint::getAirpressureQFE );
    
    private void removeOldData() {
        final long now = System.currentTimeMillis();
        aplist.removeIf((airpressurePoint) -> (now - airpressurePoint.date) > H1_IN_MSEC);
    }
    
    void add(AirpressurePoint cap) {
        removeOldData();
        aplist.add(cap);
    }

    public AirpressurePoint getMin() {
        return aplist.stream().min(comparator).get();
    }

    public AirpressurePoint getMax() {
        return aplist.stream().max(comparator).get();
    }

    public int getSize() {
        return aplist.size();
    }

}
