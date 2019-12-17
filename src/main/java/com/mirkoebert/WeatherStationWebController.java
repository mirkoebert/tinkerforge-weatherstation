package com.mirkoebert;

import com.mirkoebert.openweather.OpenWeatherService;
import com.mirkoebert.openweather.send.Sender;
import com.mirkoebert.weather.WeatherModel;
import com.mirkoebert.weather.WeatherMonitor;

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
    @Autowired
    private WeatherModel m;
    @Autowired
    private WeatherMonitor monitor;
    @Autowired 
    private Sender sender;

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

    @Value("${weatherstation.mode.alarmfashing}")
    private String alarmflashingmode;

    @Value("${openweather.enable}")
    private String openweather_enable;

    @Value("${openweather.station_id}")
    private String openweather_stationid;

    @Autowired
    private OpenWeatherService ows;
    
    @GetMapping("/")
    public ModelAndView home(Map<String, Object> model) {
        log.info("HTTP Request");
        model.put("airpressure", (int) Math.round(m.getAirPressureQFE()));
        model.put("airpressuretrend", monitor.getAirpPressureTrend());
        model.put("humidity", m.getHumdidity());
        model.put("tempInn", m.getTempIn());
        model.put("lum", m.getIllumination());
        model.put("forecast", monitor.getMessage());
        model.put("date", DateX.getInstance().getDateString());
        model.put("startDate", w.getStartDate());
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
        model.put("startDate", w.getStartDate());
        model.put("applicationName", applicationName);
        model.put("buildVersion", buildVersion);
        model.put("online", DateX.getInstance().isOnline());
        model.put("altitude", altitude);
        model.put("latitude", latitude);
        model.put("longitude", longitude);
        model.put("nightmode", nightmode);
        model.put("alarmflashingmode", alarmflashingmode);

        model.put("openweather_enable" , openweather_enable);
        model.put("openweather_stationid", openweather_stationid);
        
        model.put("openweather_name", "");
        model.put("openweather_latitude", "");
        model.put("openweather_longitude", "");

        model.put("openweather_sendcount", sender.getSendCount());
        return "info";
    }

    @GetMapping("/setup")
    public String setup(Map<String, Object> model) {
        return "setup";
    }

}
