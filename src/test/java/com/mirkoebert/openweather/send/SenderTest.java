package com.mirkoebert.openweather.send;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

class SenderTest {

    @Test
    void test() throws JsonProcessingException, JSONException {
        long now = System.currentTimeMillis() / 1000;
        Measurement m = new Measurement("testStationId");
        m.setPressure(23);
        Sender s = new Sender();
        String j = s.createJasonFromObject(m);
        System.out.println(j);
        assertNotNull(j);
        JSONAssert.assertEquals("{\"station_id\":\"testStationId\",\"dt\":"+now+",\"pressure\":23}",j, false);
        
        assertEquals(s.getSendCount(), 0L);
        
        
        assertFalse(s.sendCurrentWeatherToOpenWeather());
        assertNull(s.getWeatherStationFromOpenWeather());
    }

}
