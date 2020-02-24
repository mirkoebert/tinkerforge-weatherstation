package com.mirkoebert.weather.tinkerforge;

import lombok.Getter;
import lombok.Setter;

public class TinkerforgeWeather {

    @Getter
    @Setter
    double tempIn = -273;
    @Getter
    @Setter
    long illumination = -1;
    @Getter
    double airPressureQFE = -1;
    @Getter
    @Setter
    double humdidity = -1;

}
