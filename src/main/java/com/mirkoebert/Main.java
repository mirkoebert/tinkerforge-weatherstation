package com.mirkoebert;

import lombok.extern.slf4j.Slf4j;

import com.mirkoebert.weather.AirPressurePointRepository;
import com.mirkoebert.weather.WeatherModel;
import com.mirkoebert.weather.WeatherMonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
@Slf4j
public class Main {

    public static void main(String[] args) {
//        AirPressurePointRepository r = new AirPressurePointRepository();
//        WeatherModel w = new WeatherModel(r);
//        WeatherMonitor f = new WeatherMonitor(r,w);
//        
//        WeatherStation s = new WeatherStation(w,f);
        
        SpringApplication.run(Main.class, args);
        log.info("App start");
    }

}
