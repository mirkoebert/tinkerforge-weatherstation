package com.ebertp;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
public class WeatherStationWebController {
	
  //private WeatherStation w = WeatherStation.getInstance();
  
  @Autowired
  private WeatherStation w ;
	
  
	@GetMapping("/")
	public ModelAndView home(Map<String, Object> model) {
		log.info("HTTP Request");
		WeatherModel m = w.getWeatherModel();
		model.put("airpressure", (int)Math.round(m.getAirPressure()) );
		model.put("humidity", m.getHumdidity());
		model.put("tempInn", m.getTempIn());
		model.put("lum", m.getIllumination());
		model.put("forecast", m.getForecast());
		model.put("date", DateX.getInstance().getDateString());
		log.info("HTTP Request done: "+model.toString());
		return new ModelAndView ("index",model);
	}
	
	@GetMapping("/webcams")
	public String cams(Map<String, Object> model) {
		model.put("date", DateX.getInstance().getDateString());
		return "webcams";
	}
	

}
