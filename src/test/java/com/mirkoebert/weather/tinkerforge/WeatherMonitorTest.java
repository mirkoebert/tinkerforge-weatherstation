package com.mirkoebert.weather.tinkerforge;

import static org.junit.jupiter.api.Assertions.*;

import com.mirkoebert.weather.tinkerforge.AirPressureTrend;
import com.mirkoebert.weather.tinkerforge.TinkerforgeWeatherMonitor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class WeatherMonitorTest  {

    @Autowired
    TinkerforgeWeatherMonitor wm;
    

    @Test
    void testGetMessage() {
        assertEquals(TinkerforgeWeatherMonitor.NO_FORECAST, wm.getMessage());
    }

    @Test
    void testGetAirpPressureTrend() {
        assertEquals(AirPressureTrend.unknown.toString(), wm.getAirpPressureTrend());
    }

    @Test
    void testGetForeCast() {
        assertEquals(TinkerforgeWeatherMonitor.NO_FORECAST, wm.getForeCast());
    }

    @Test
    void testIsAlarm() {
        assertFalse(wm.isAlarm());
    }

}
