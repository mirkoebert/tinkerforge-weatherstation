package com.mirkoebert.weather.openweather.http;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
final class Measurement {

    final String station_id;
    final long dt = System.currentTimeMillis() / 1000;
    private int pressure = 0;

    public Measurement(final String station_id, final int pressure) {
        this.station_id = station_id;
        this.pressure = pressure;
    }
}
