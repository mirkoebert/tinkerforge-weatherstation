package com.mirkoebert.openweather.send;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirkoebert.openweather.WeatherStation;
import com.mirkoebert.weather.WeatherModel;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Order(120)
@Slf4j
public class Sender {

    @Value("${info.app.name}")
    private String applicationName;
    @Value("${openweather.station_id}")
    private String station_id;
    @Value("${openweather.APPID}")
    private String APPID;
    @Value("${openweather.enable}")
    private boolean enable;


    @Autowired
    private WeatherModel m;
    
    @Getter
    private long sendCount = 0;


    String createJasonFromObject(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }


    private void sendPOST(final String json) throws IOException, InterruptedException {
        sendCount++;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://api.openweathermap.org/data/3.0/measurements?APPID=" + APPID);


        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Content-Type", "application/json");

        CloseableHttpResponse response = client.execute(httpPost);
        int rc = response.getStatusLine().getStatusCode();
        if (rc > 299) {
            log.warn("Send data to OpenWeather error. Http status error code: " + rc );
        }
        client.close();
    }


    private HttpEntity sendGET() throws IOException, InterruptedException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://api.openweathermap.org/data/3.0/stations/" + station_id + "?APPID=" + APPID);

        CloseableHttpResponse response = client.execute(httpGet);
        int rc = response.getStatusLine().getStatusCode();
        if (rc > 299) {
            log.warn("Can't getstation info from OpenWeather error. Http status error code: " + rc );
        }
        HttpEntity entity = response.getEntity();
        client.close();
        return entity;
    }
    
    
    
    @Scheduled(initialDelay = 15000, fixedDelay = 300000)
    public void sendCurrentWeatherToOpenWeather() {
        if (enable) {
            log.info("Send data to OpenWeather.");
            Measurement me = new Measurement(station_id);
            me.setPressure((int)Math.round(m.getAirPressureQFE()));

            try {
                sendPOST("[" + createJasonFromObject(me) + "]");
            } catch (IOException | InterruptedException e) {
                log.error("Can't send data to OpenWeather server.",e);
            }
        }
    }

    
    public WeatherStation getWeatherStationFromOpenWeather() {
        if (enable) {
            log.info("Gaet station info from OpenWeather.");
            try {
                HttpEntity resp = sendGET();
                //String retSrc = EntityUtils.toString(resp);
                ObjectMapper mapper = new ObjectMapper();

                // Read JSON file and convert to java object
                InputStream fileInputStream = resp.getContent();
                WeatherStation wsow = mapper.readValue(fileInputStream, WeatherStation.class);
                fileInputStream.close();
                return wsow;
            } catch (IOException | InterruptedException e) {
                log.error("Can't send data to OpenWeather server.",e);
                
            }
        }
        return null;
    }



}
