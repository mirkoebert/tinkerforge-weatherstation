package com.ebertp;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
@EnableAutoConfiguration
@SpringBootApplication
public class WeatherStationController {
	
	
	@GetMapping("/")
	String home(Map<String, Object> model) {
		WeatherStation w = WeatherStation.getInstance();
		WeatherModel m = w.getModell();
		System.out.println("test");
		model.put("airpressure", (int)Math.round(m.getAirPressure()) );
		model.put("humidity", m.getHumdidity());
		model.put("tempInn", m.getTempIn());
		model.put("lum", m.getIllumination());
		model.put("forecast", m.getForecast());
		model.put("date", DateX.getInstance().getDateString());
		return "index";
	}
	
	@GetMapping("/webcams")
	String cams(Map<String, Object> model) {
		model.put("date", DateX.getInstance().getDateString());
		return "webcams";
	}
	

	public static void main(String[] args) throws Exception {
		WeatherStation w = WeatherStation.getInstance();
        	SpringApplication.run(WeatherStationController.class, args);
    }
}
