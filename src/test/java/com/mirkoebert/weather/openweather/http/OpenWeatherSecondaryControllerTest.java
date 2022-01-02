package com.mirkoebert.weather.openweather.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirkoebert.weather.openweather.OpenWeatherWeather;
import com.mirkoebert.weather.tinkerforge.TinkerforgeWeather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

class OpenWeatherSecondaryControllerTest {

    private final OpenWeatherSecondaryController sut = new OpenWeatherSecondaryController(
            new ObjectMapper(),
            new TinkerforgeWeather(null));

    @Test
    void test() throws JsonProcessingException, JSONException {
        final long now = System.currentTimeMillis() / 1000;
        final Measurement m = new Measurement("testStationId", 23);

        final String j = sut.createJasonFromObject(m);
        System.out.println(j);
        assertNotNull(j);
        JSONAssert.assertEquals("{\"station_id\":\"testStationId\",\"dt\":" + now + ",\"pressure\":23}", j, false);

        assertEquals(sut.getSendCount(), 0L);

        assertFalse(sut.sendCurrentWeatherToOpenWeather());
        assertNull(sut.getWeatherStationFromOpenWeather());
    }

    @Test
    void testMapper() throws JsonParseException, JsonMappingException, IOException {
        final OpenWeatherWeather oww = sut.convertJsonStringToObject(getStringFromFilename("openweatherweather.json"));
        assertEquals("Rukieten", oww.getName());
        assertEquals(7.27f, oww.getTemp(), 0.01f);
        assertEquals(6.11f, oww.getMinTemp(), 0.01f);
        assertEquals(8f, oww.getMaxTemp(), 0.01f);
        assertEquals(1008f, oww.getPressure(), 0.01f);
        assertEquals(65f, oww.getHumidity(), 0.01f);
        assertEquals("Mäßig bewölkt", oww.getDescription());

        assertNull(sut.convertJsonStringToObject(null));
    }

    private String getStringFromFilename(String fileName) throws IOException {
        final BufferedReader br = new BufferedReader(getReaderFromFilename(fileName));
        String sCurrentLine;
        StringBuilder contentBuilder = new StringBuilder();
        while ((sCurrentLine = br.readLine()) != null) {
            contentBuilder.append(sCurrentLine).append("\n");
        }
        br.close();
        return contentBuilder.toString();
    }

    private Reader getReaderFromFilename(String fileName) throws IOException {
        final InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        return new InputStreamReader(in, "UTF-8");
    }

}
