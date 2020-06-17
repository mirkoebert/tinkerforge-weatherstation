package com.mirkoebert.controllerIn;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class DateX {

    private static final DateTimeFormatter TIEM_ONLY_PATTERN = DateTimeFormatter.ofPattern("EE HH:mm:ss");
    private static final DateTimeFormatter DATE_ONLY_PATTERN = DateTimeFormatter.ofPattern("d. MMMM");
    private static final ZoneId TIMEZONE_ID = ZoneId.of("Europe/Berlin");
    public static final DateTimeFormatter DATE_TIME_FORMATER = DateTimeFormatter.ofPattern("EE HH:mm:ss d. MMMM YYYY");
    private static DateX instance = null;
    private boolean isOnline = false;

    private DateX() {
        isOnline = isOnline();
        
    }

    
    public String getDateString() {
        String r = "";
        ZonedDateTime zdt = ZonedDateTime.now(TIMEZONE_ID);
        if (isOnline) {
            r = zdt.format(DATE_TIME_FORMATER);
        } else {
            isOnline = isOnline();
            if (isOnline) {
                r = zdt.format(DATE_TIME_FORMATER);
            }
        }
        return r;
    }

    /**
     * Check if weather station is online. Try to reach an internet server.
     * If station is online, the date and time are correct.
     * @return 
     */
    public boolean isOnline() {
        boolean r = false;
        try {
            final URL url = new URL("http://www.ebert-p.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            r = true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
        return r;
    }

    public static DateX getInstance() {
        if (instance == null) {
            instance = new DateX();
        }
        return instance;
    }

    public String getDateOnlyString() {
        String r = "";
        if (isOnline) {
            ZonedDateTime zdt = ZonedDateTime.now(TIMEZONE_ID);
            r = zdt.format(DATE_ONLY_PATTERN);
        }
        return r;
    }

    public String getTimeOnlyString() {
        String r = "";
        if (isOnline) {
            ZonedDateTime zdt = ZonedDateTime.now(TIMEZONE_ID);
            r = zdt.format(TIEM_ONLY_PATTERN);
        }
        return r;
    }
}