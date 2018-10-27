package com.ebertp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

final class DateX {

    private static DateX instance = null;
    private SimpleDateFormat sdf1 = new SimpleDateFormat("EE HH:mm:ss d. MMMM YYYY");
    private boolean isOnline = false;

    private DateX() {
        isOnline = isOnline();
    }

    String getDateString() {
        String r = "";
        if (isOnline) {
            r = sdf1.format(new Date());
        } else {
            isOnline = isOnline();
            if (isOnline) {
                r = sdf1.format(new Date());
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