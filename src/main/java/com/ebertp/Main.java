package com.ebertp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import lombok.extern.slf4j.Slf4j;

@EnableAutoConfiguration
@SpringBootApplication
@Slf4j
public class Main {

  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(Main.class, args);
    log.info("App start");
    WeatherStation w = WeatherStation.getInstance();
  }
  
}
