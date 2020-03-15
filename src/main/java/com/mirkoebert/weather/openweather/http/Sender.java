package com.mirkoebert.weather.openweather.http;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirkoebert.weather.openweather.OpenWeatherWeather;
import com.mirkoebert.weather.tinkerforge.TinkerforgeWeather;

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
    @Value("${openweather.send}")
    private boolean enableSend;
    @Value("${tinkerforge.enable}")
    private boolean enableTinkerforge;

    @Autowired
    private TinkerforgeWeather weatehrModel;
    @Getter
    private int sendCount = 0;
    @Getter
    private int sendErrorCount = 0;


    String createJasonFromObject(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }


    private void sendPOST(final String json) throws Exception   {
        HttpPost httpPost = new HttpPost("http://api.openweathermap.org/data/3.0/measurements?APPID=" + APPID);
        if (json == null) {
            log.warn("Json String is empty.");
        } else {
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
        }
        httpPost.setHeader("Content-Type", "application/json");


        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(httpPost);
        final int rc = response.getStatusLine().getStatusCode();
        response.close();
        client.close();
        if (rc > 299) {
            throw new RuntimeException("Send data to OpenWeather error. Http status error code: " + rc);
        }
    }


    private String sendGET(final String completeUrlString) throws Exception {
        String retSrc = null;

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(completeUrlString);
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



    @Scheduled(initialDelay = 240000, fixedDelay = 300000)
    public boolean sendCurrentWeatherToOpenWeather() {
        if (enable && enableSend && enableTinkerforge) {
            log.info("Send data to OpenWeather.");
            Measurement me = new Measurement(station_id);
            me.setPressure((int)Math.round(weatehrModel.getAirPressureQFE()));

            try {
                sendPOST("[" + createJasonFromObject(me) + "]");
                sendCount++;
                return true;
            } catch (Exception e) {
                sendErrorCount++;
                log.error("Can't send data to OpenWeather server.",e);
            }
        } else {
            log.info("OpenWeather sending data diabled.");            
        }
        return false;
    }


    public OpenWeatherWeatherStation getWeatherStationFromOpenWeather() {
        if (enable) {
            log.info("Get station info from OpenWeather.");
            ObjectMapper mapper = new ObjectMapper();
            try {
                String retSrc = sendGET("http://api.openweathermap.org/data/3.0/stations/" + station_id + "?APPID=" + APPID);
                InputStream fileInputStream = new ByteArrayInputStream(retSrc.getBytes(StandardCharsets.UTF_8));
                OpenWeatherWeatherStation wsow = mapper.readValue(fileInputStream, OpenWeatherWeatherStation.class);
                fileInputStream.close();            

                return wsow;
            } catch (Exception e) {
                log.error("Can't get station data to OpenWeather server.",e);

            }
        }
        return null;
    }


    public OpenWeatherWeather getWeatherForWeatherDStationCoordinates() {
        if (enable) {
            log.info("Get station info from OpenWeather.");
            try {
                String retSrc = sendGET("http://api.openweathermap.org/data/2.5/weather?lat=53.894658&lon=12.183633&units=metric&lang=de&appid=" + APPID);
                OpenWeatherWeather wsow = convertJsonStringToObject(retSrc);            
                return wsow;
            } catch (Exception e) {
                log.error("Can't get weather from OpenWeather server.",e);

            }
        }
        return null;
    }


    OpenWeatherWeather convertJsonStringToObject(String retSrc)
            throws IOException, JsonParseException, JsonMappingException {
        InputStream fileInputStream = new ByteArrayInputStream(retSrc.getBytes(StandardCharsets.UTF_8));
        ObjectMapper mapper = new ObjectMapper();
        OpenWeatherWeather wsow = mapper.readValue(fileInputStream, OpenWeatherWeather.class);
        fileInputStream.close();
        return wsow;
    }

}
