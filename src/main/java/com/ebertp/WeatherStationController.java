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

	@GetMapping("/test")
	String home(Map<String, Object> model) {
		WeatherStation w = WeatherStation.getInstance();
		WeatherModel m = w.getModell();
		System.out.println("test");
		model.put("airpressure", (int)Math.round(m.getAirPressure()) );
		model.put("humidity", m.getHumdidity());
		model.put("tempInn", m.getTempIn());
		model.put("lum", m.getIllumination());
		model.put("forecast", m.getForecast());
		return "welcome";
	}
	
	

	public static void main(String[] args) throws Exception {
        SpringApplication.run(WeatherStationController.class, args);
    }
}
