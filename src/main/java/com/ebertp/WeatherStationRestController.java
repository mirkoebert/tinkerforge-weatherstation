package com.ebertp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherStationRestController {

    @Autowired
    private WeatherStation w;

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
