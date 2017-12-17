package com.ebertp;

import java.text.SimpleDateFormat;
import java.util.Date;
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

	private SimpleDateFormat sdf1 = new SimpleDateFormat("EE HH:mm:ss d. MMMM YYYY");
	
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
		model.put("date", sdf1.format(new Date()));
		return "index";
	}
	
	@GetMapping("/webcams")
	String cams(Map<String, Object> model) {
		model.put("date", sdf1.format(new Date()));
		return "webcams";
	}
	

	public static void main(String[] args) throws Exception {
		WeatherStation w = WeatherStation.getInstance();
        	SpringApplication.run(WeatherStationController.class, args);
    }
}
