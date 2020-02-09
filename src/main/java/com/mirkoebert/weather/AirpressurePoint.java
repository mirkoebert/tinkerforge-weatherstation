package com.mirkoebert.weather;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public final class AirpressurePoint {

    final long date;
    final double airpressureQFE;

}
