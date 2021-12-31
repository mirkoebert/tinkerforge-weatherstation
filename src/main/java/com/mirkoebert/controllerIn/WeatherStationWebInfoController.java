package com.mirkoebert.controllerIn;

import com.mirkoebert.weather.openweather.OpenWeatherService;
import com.mirkoebert.weather.openweather.http.OpenWeatherWeatherStation;
import com.mirkoebert.weather.openweather.http.Sender;
import com.mirkoebert.weather.tinkerforge.TinkerforgeWeatherStation;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@Lazy
public class WeatherStationWebInfoController {

    private final TinkerforgeWeatherStation tinkerforgeWeatherStation;

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

    private final OpenWeatherService owss;

    @GetMapping("/info")
    public String info(Map<String, Object> model) {
        model.put("date", DateX.getInstance().getDateString());
        model.put("startDate", DateX.DATE_TIME_FORMATER.format(tinkerforgeWeatherStation.getStartDate()));
        model.put("applicationName", applicationName);
        model.put("buildVersion", buildVersion);
        model.put("online", DateX.getInstance().isOnline());
        model.put("altitude", altitude);
        model.put("nightmode", nightmode);
        model.put("alarmflashingmode", alarmflashingmode);

        model.put("openweather_enable", openweather_enable);
        if (openweather_enable) {
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
