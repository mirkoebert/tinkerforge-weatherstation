package com.mirkoebert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@EnableAutoConfiguration
@SpringBootApplication
@Slf4j
public class Main {

    public static void main(String[] args) {        
        SpringApplication.run(Main.class, args);
        log.info("App start");
    }

}
