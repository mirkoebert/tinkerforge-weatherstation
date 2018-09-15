package com.ebertp;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
public class WeatherStationController {
	
  WeatherStation w = WeatherStation.getInstance();
	
	@GetMapping("/")
	String home(Map<String, Object> model) {
		log.info("HTTP Request");
		WeatherModel m = w.getModell();
		model.put("airpressure", (int)Math.round(m.getAirPressure()) );
		model.put("humidity", m.getHumdidity());
		model.put("tempInn", m.getTempIn());
		model.put("lum", m.getIllumination());
		model.put("forecast", m.getForecast());
		model.put("date", DateX.getInstance().getDateString());
		log.info("HTTP Request done");
		return "index";
	}
	
	@GetMapping("/webcams")
	String cams(Map<String, Object> model) {
		model.put("date", DateX.getInstance().getDateString());
		return "webcams";
	}
	

}
