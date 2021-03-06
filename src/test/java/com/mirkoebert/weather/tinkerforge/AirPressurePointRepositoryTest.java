package com.mirkoebert.weather.tinkerforge;

import static org.junit.jupiter.api.Assertions.*;

import com.mirkoebert.weather.tinkerforge.AirPressurePointRepository;
import com.mirkoebert.weather.tinkerforge.AirpressurePoint;

import org.junit.jupiter.api.Test;

class AirPressurePointRepositoryTest {

    @Test
    void testGetMin() {
        AirpressurePoint ap1 = new AirpressurePoint(1);
        AirpressurePoint ap2 = new AirpressurePoint(2);
        AirPressurePointRepository repo = new AirPressurePointRepository();
        repo.add(ap2);
        repo.add(ap1);
        
        AirpressurePoint res = repo.getMin();
        
        assertEquals(1, res.airpressureQFE);
    }

}
