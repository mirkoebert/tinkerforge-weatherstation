package com.mirkoebert.weather;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class WeatherModelTest {

    @Autowired
    private WeatherModel m;

    @Test
    void test() {        
        assertThrows(IllegalArgumentException.class,
                () -> m.setAirPressureQFE(-1),
                "Airpressure value is allways positive");
    }

}
