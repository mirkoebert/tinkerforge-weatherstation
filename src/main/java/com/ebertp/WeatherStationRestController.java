package com.ebertp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherStationRestController {

    @Autowired
    private WeatherStation w;

    @Value("${info.app.name}")
    private String applicationName;

    @Value("${info.app.version}")
    private String buildVersion;
    
    @Value("${weatherstation.position.latitude}")
    private String latitude;
    
    @Value("${weatherstation.position.longitude}")
    private String longitude;

    @Value("${weatherstation.position.altitude}")
    private String altitude;

    @Value("${weatherstation.mode.nightmode}")
    private String nightmode;
    
    @Value("${weatherstation.mode.alarmfashing}")
    private String alarmflashingmode;

    
    @GetMapping("/v1/weatherdata")
    @ResponseBody
    public WeatherModel weatherdata() {
        WeatherModel m = w.getWeatherModel();
        return m;
    }

    @GetMapping("/v1/weatherstation")
    @ResponseBody
    public WeatherStation weatherstation() {
        return w;
    }

    @GetMapping("/v1/airpressure")
    @ResponseBody
    public List<AirpressurePoint> airpressure() {
        return AirPressurePointRepository.getINSTANCE().getAplist();
    }

}
