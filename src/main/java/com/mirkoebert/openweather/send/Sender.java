package com.mirkoebert.openweather.send;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirkoebert.openweather.WeatherStation;
import com.mirkoebert.weather.WeatherModel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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


    private String sendGET() throws IOException, InterruptedException {
        String retSrc = null;

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://api.openweathermap.org/data/3.0/stations/" + station_id + "?APPID=" + APPID);
        log.info("uri: " + httpGet.getURI());

        CloseableHttpResponse response = client.execute(httpGet);
        int rc = response.getStatusLine().getStatusCode();
        if (rc > 299) {
            log.warn("Can't get station info from OpenWeather error. Http status error code: " + rc );
        } else {
            log.info("Http status code: " + rc );
            HttpEntity entity = response.getEntity();
            retSrc =  EntityUtils.toString(entity);
        }
        client.close();
        return retSrc;
    }



    @Scheduled(initialDelay = 120000, fixedDelay = 300000)
    public boolean sendCurrentWeatherToOpenWeather() {
        if (enable) {
            log.info("Send data to OpenWeather.");
            Measurement me = new Measurement(station_id);
            me.setPressure((int)Math.round(m.getAirPressureQFE()));

            try {
                sendPOST("[" + createJasonFromObject(me) + "]");
                return true;
            } catch (IOException | InterruptedException e) {
                log.error("Can't send data to OpenWeather server.",e);
            }
        } else {
            log.info("OpenWeather integration diabled.");            
        }
        return false;
    }


    public WeatherStation getWeatherStationFromOpenWeather() {
        if (enable) {
            log.info("Get station info from OpenWeather.");
            try {
                ObjectMapper mapper = new ObjectMapper();

                String retSrc = sendGET();
                InputStream fileInputStream = new ByteArrayInputStream(retSrc.getBytes(StandardCharsets.UTF_8));
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
