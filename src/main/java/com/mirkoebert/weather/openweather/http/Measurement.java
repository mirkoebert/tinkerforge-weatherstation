package com.mirkoebert.weather.openweather.http;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
final class Measurement {

    final String station_id;
    final long dt = System.currentTimeMillis() / 1000;
    @Setter
    private int pressure = 0;
    
    public Measurement(String station_id) {
        this.station_id = station_id;
    }
}
