package com.mirkoebert.controllerIn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirkoebert.weather.WeatherService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@Lazy
public class WeatherStationWebWeatherController {
 
    @Autowired
    private WeatherService ws;

    @GetMapping("/")
    public ModelAndView home(Map<String, Object> model) throws JsonProcessingException {
        log.info("HTTP Request");
        ObjectMapper mapper = new ObjectMapper();
        model = mapper.convertValue(ws.getWeather(), Map.class);
        log.info("HTTP Request done: " + model.toString());
        return new ModelAndView("index", model);
    }

    @GetMapping("/webcams")
    public String cams(Map<String, Object> model) {
        model.put("date", DateX.getInstance().getDateString());
        return "webcams";
    }

 

}
