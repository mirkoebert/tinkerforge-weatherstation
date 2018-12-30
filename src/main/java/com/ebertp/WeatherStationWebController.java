package com.ebertp;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class WeatherStationWebController {

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

    @GetMapping("/")
    public ModelAndView home(Map<String, Object> model) {
        log.info("HTTP Request");
        WeatherModel m = w.getWeatherModel();
        model.put("airpressure", (int) Math.round(m.getAirPressure()));
        model.put("humidity", m.getHumdidity());
        model.put("tempInn", m.getTempIn());
        model.put("lum", m.getIllumination());
        model.put("forecast", m.getForecast());
        model.put("date", DateX.getInstance().getDateString());
        log.info("HTTP Request done: " + model.toString());
        return new ModelAndView("index", model);
    }

    @GetMapping("/webcams")
    public String cams(Map<String, Object> model) {
        model.put("date", DateX.getInstance().getDateString());
        return "webcams";
    }

    @GetMapping("/info")
    public String info(Map<String, Object> model) {
        model.put("date", DateX.getInstance().getDateString());
        model.put("applicationName", applicationName);
        model.put("buildVersion", buildVersion);
        model.put("online", DateX.getInstance().isOnline());
        model.put("altitude", altitude);
        model.put("latitude", latitude);
        model.put("longitude", longitude);
        model.put("nightmode", nightmode);
        return "info";
    }

    @GetMapping("/setup")
    public String setup(Map<String, Object> model) {
        return "setup";
    }

}
