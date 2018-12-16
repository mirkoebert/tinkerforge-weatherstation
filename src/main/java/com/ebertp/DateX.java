package com.ebertp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
final class DateX {

    private static DateX instance = null;
    private ZoneId zoneId = ZoneId.of( "Europe/Berlin" );
    private boolean isOnline = false;

    private DateX() {
        isOnline = isOnline();
    }

    String getDateString() {
        ZonedDateTime zdt = ZonedDateTime.now( zoneId ) ;
        log.info("QQQ: "+zdt);
        log.info("QQQ: "+zdt.format(DateTimeFormatter.ofPattern("EE HH:mm:ss d. MMMM YYYY")));
        String r = "";
        if (isOnline) {
            r = zdt.format(DateTimeFormatter.ofPattern("EE HH:mm:ss d. MMMM YYYY"));
        } else {
            isOnline = isOnline();
            if (isOnline) {
                r = zdt.format(DateTimeFormatter.ofPattern("EE HH:mm:ss d. MMMM YYYY"));
            }
        }
        return r;
    }

    private boolean isOnline() {
        boolean r = false;
        try {
            final URL url = new URL("http://www.otto.de");
            final URLConnection conn = url.openConnection();
            conn.connect();
            r = true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
        isOnline = r;
        return r;
    }

    static DateX getInstance() {
        if (instance == null) {
            instance = new DateX();
        }
        return instance;
    }

    public static void main(String[] args) throws Exception {
        String s = DateX.getInstance().getDateString();
        System.out.println("XXX: " + s);
    }
}