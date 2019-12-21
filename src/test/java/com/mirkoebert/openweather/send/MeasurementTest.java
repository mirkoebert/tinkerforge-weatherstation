package com.mirkoebert.openweather.send;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MeasurementTest {

    @Test
    void testDtIsUnixts() {
        Measurement m = new Measurement("XX");
        assertTrue(m.getDt() < 15762552910L);
        assertTrue(m.getDt() > 157625529L);
    }

}