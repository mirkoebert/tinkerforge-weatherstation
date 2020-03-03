package com.mirkoebert.controllerIn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirkoebert.WeatherStation;
import com.mirkoebert.weather.WeatherService;
import com.mirkoebert.weather.openweather.OpenWeatherService;
import com.mirkoebert.weather.openweather.OpenWeatherWeatherStation;
import com.mirkoebert.weather.openweather.http.Sender;

import java.util.Date;
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
    private Sender sender;

    @Value("${info.app.name}")
    private String applicationName;

    @Value("${info.app.version}")
    private String buildVersion;

    @Value("${weatherstation.position.altitude}")
    private String altitude;

    @Value("${weatherstation.mode.nightmode}")
    private String nightmode;

    @Value("${weatherstation.mode.alarmfashing}")
    private String alarmflashingmode;

    @Value("${openweather.enable}")
    private boolean openweather_enable;

    @Value("${openweather.station_id}")
    private String openweather_stationid;

    @Autowired
    private OpenWeatherService owss;
    
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

    @GetMapping("/info")
    public String info(Map<String, Object> model) {
        model.put("date", DateX.getInstance().getDateString());
        model.put("startDate",  DateX.DATE_TIME_FORMATER.format(w.getStartDate()));
        model.put("applicationName", applicationName);
        model.put("buildVersion", buildVersion);
        model.put("online", DateX.getInstance().isOnline());
        model.put("altitude", altitude);
        model.put("nightmode", nightmode);
        model.put("alarmflashingmode", alarmflashingmode);

        model.put("openweather_enable" , openweather_enable);
        if(openweather_enable) {
            model.put("openweather_stationid", openweather_stationid);

            OpenWeatherWeatherStation ows = owss.getStation();
            if (ows != null) {
                model.put("openweather_name", ows.getName());
                model.put("openweather_latitude", ows.getLatitude());
                model.put("openweather_longitude", ows.getLongitude());
            }

            model.put("openweather_sendcount", sender.getSendCount());
            model.put("openweather_senderrorcount", sender.getSendErrorCount());
        }
        return "info";
    }


}
