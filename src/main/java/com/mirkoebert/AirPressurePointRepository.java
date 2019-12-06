package com.mirkoebert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;

public class AirPressurePointRepository {

    @Getter
    private List<AirpressurePoint> aplist = new ArrayList<AirpressurePoint>();
    static final long H1_IN_MSEC = 60 * 60 * 1000;

    @Getter
    private static final AirPressurePointRepository INSTANCE = new AirPressurePointRepository();
    
    private void removeOldData() {
        long now = System.currentTimeMillis();
        for (Iterator<AirpressurePoint> iterator = aplist.iterator(); iterator.hasNext();) {
            AirpressurePoint airpressurePoint = iterator.next();
            if ((now - airpressurePoint.date) > H1_IN_MSEC) {
                iterator.remove();
            }
        }
    }
    
    AirpressurePoint getMin(AirpressurePoint current) {
        AirpressurePoint min = current;
        for (AirpressurePoint airpressurePoint : aplist) {
            if (airpressurePoint.airpressureQFE < min.airpressureQFE) {
                min = airpressurePoint;
            }
        }
        return min;
    }

    AirpressurePoint getMax(AirpressurePoint current) {
        AirpressurePoint max = current;
        for (AirpressurePoint airpressurePoint : aplist) {
            if (airpressurePoint.airpressureQFE > max.airpressureQFE) {
                max = airpressurePoint;
            }
        }
        return max;
    }
    
    void add(AirpressurePoint cap) {
        removeOldData();
        aplist.add(cap);
    }

}
