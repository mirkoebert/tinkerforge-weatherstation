package com.mirkoebert;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(WeatherStationRestController.class)
class WeatherStationRestControllerTest {

    @Autowired
    private MockMvc mvc;
 
    @MockBean
    private WeatherStationRestController w;
    
    
    @Test
    void testUrl() throws Exception {
        mvc.perform(get("/v1/weatherdata")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        
        mvc.perform(get("/v1/weatherstation")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    
        mvc.perform(get("/v1/airpressure")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(get("/v1/xxx")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

    }
    

}
