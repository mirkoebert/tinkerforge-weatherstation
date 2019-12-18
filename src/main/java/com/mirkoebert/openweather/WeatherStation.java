package com.mirkoebert.openweather;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WeatherStation {
    
    private String id; //": "5df37e046c634e00011dff22",
    private Date created_at; //: "2019-12-13T12:03:16.034Z",
    private Date updated_at; //: "2019-12-13T12:03:16.034Z",
    private String external_id; //: "KlingendorfTEST001",
    private String name; //": "Klingendorf",
    private double longitude;//: 12.18,
    private double latitude; //": 53.98,
    private int altitude; // 25,
    private int rank; //: 10


}
