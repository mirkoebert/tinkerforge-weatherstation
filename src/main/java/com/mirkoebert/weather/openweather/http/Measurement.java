package com.mirkoebert.weather.openweather.http;

import lombok.Value;

@Value
final class Measurement {

    String station_id;
    final long dt = System.currentTimeMillis() / 1000;
    int pressure;

}
