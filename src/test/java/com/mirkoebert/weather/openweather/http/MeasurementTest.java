package com.mirkoebert.weather.openweather.http;

import static org.junit.jupiter.api.Assertions.*;

import com.mirkoebert.weather.openweather.http.Measurement;

import org.junit.jupiter.api.Test;

class MeasurementTest {

    @Test
    void testDtIsUnixts() {
        Measurement m = new Measurement("XX");
        assertTrue(m.getDt() < 15762552910L);
        assertTrue(m.getDt() > 157625529L);
        
        assertEquals(0, m.getPressure());
        assertEquals("XX", m.getStation_id());
    }

}
