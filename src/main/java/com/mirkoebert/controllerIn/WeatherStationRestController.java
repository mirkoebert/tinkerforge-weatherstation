package com.mirkoebert.controllerIn;

import com.mirkoebert.WeatherStation;
import com.mirkoebert.weather.AirPressurePointRepository;
import com.mirkoebert.weather.AirpressurePoint;
import com.mirkoebert.weather.WeatherModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Order(100)
public class WeatherStationRestController {

    @Autowired
    private WeatherStation w;
    @Autowired
    private AirPressurePointRepository apr;
    @Autowired
    private WeatherModel m;

    @GetMapping("/v1/weatherdata")
    @ResponseBody
    public WeatherModel weatherdata() {
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
        return apr.getAplist();
    }

}
