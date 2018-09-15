package com.ebertp;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WeatherStationControllerWeb {

    @Autowired
    WeatherStation weatherStation;

    @GetMapping("/")
    String home(Map<String, Object> model) {
        WeatherModel weatherModel = weatherStation.getModell();
        System.out.println("test");
        model.put("airpressure", (int) Math.round(weatherModel.getAirPressure()));
        model.put("humidity", weatherModel.getHumdidity());
        model.put("tempInn", weatherModel.getTempIn());
        model.put("lum", weatherModel.getIllumination());
        model.put("forecast", weatherModel.getForecast());
        model.put("warning", weatherModel.getWarning());
        model.put("date", DateX.getInstance().getDateString());
        return "index";
    }

    @GetMapping("/webcams")
    String cams(Map<String, Object> model) {
        model.put("date", DateX.getInstance().getDateString());
        return "webcams";
    }

    
}
