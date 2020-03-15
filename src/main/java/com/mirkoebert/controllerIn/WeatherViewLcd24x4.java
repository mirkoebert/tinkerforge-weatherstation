package com.mirkoebert.controllerIn;

import com.mirkoebert.weather.tinkerforge.TinkerforgeWeather;
import com.mirkoebert.weather.tinkerforge.WeatherMonitor;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.TinkerforgeException;

import java.text.DecimalFormat;
import java.util.Calendar;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WeatherViewLcd24x4 implements Runnable {

    private final BrickletLCD20x4 lcd;
    private final DecimalFormat df1 = new DecimalFormat("#.0");
    private final DecimalFormat df0 = new DecimalFormat("#");
    private boolean timeOrdate = true;
    @Setter
    private boolean nightmode;
    @Setter
    private boolean alarmflashingmode;
    
    private final TinkerforgeWeather weatherModell;
    private final WeatherMonitor weatherMonitor;

    public WeatherViewLcd24x4(BrickletLCD20x4 lcd, TinkerforgeWeather weatherModell, WeatherMonitor weatherMonitor) {
        this.lcd = lcd;
        this.weatherModell = weatherModell;
        this.weatherMonitor = weatherMonitor;
        final Thread t = new Thread(this);
        t.start();
    }

    public void paint() {
        try {
            lcd.clearDisplay();
            if (nightmode && isNight()) {
                lcd.backlightOff();
            } else {
                lcd.backlightOn();
                lcd.writeLine((short) 0, (short) 12, (int) Math.round(weatherModell.getAirPressureQFE()) + " hPa");
                lcd.writeLine((short) 0, (short) 0, df1.format(weatherModell.getHumdidity()) + " %RH");
                lcd.writeLine((short) 1, (short) 0, df1.format(weatherModell.getTempIn()) + " C");
                lcd.writeLine((short) 2, (short) 0, df0.format(weatherModell.getIllumination()) + " lx  ");

                String message;
                if (timeOrdate) {
                    message = DateX.getInstance().getDateOnlyString();
                } else {
                    message = DateX.getInstance().getTimeOnlyString();
                }
                lcd.writeLine((short) 1, (short) 8, message);

                StringBuilder forcast = new StringBuilder(utf16ToKS0066U(weatherMonitor.getMessage()));
                int l = forcast.length();
                if (forcast.length() < 24) {
                    for (int i = 0; i < (24 - l); i++) {
                        forcast.append(" ");
                    }
                }
                lcd.writeLine((short) 3, (short) 0, forcast.toString());
                timeOrdate = !timeOrdate;
            }
        } catch (TinkerforgeException e) {
            log.error("paint", e);
        }
    }

    private void flashBacklight() {
        try {
            lcd.backlightOff();
            Thread.sleep(500);
            lcd.backlightOn();
            Thread.sleep(1500);
            lcd.backlightOff();
            Thread.sleep(500);
            lcd.backlightOn();
        } catch (TinkerforgeException | InterruptedException e) {
            log.warn(e.getLocalizedMessage());        
        } 

    }

    private boolean isNight() {
        int h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        return (h <= 5) || (h >= 22);
    }


    @Override
    public void run() {
        while (true) {
            try {
                paint();
                if(weatherMonitor.isAlarm() && alarmflashingmode) {
                    flashBacklight();
                    Thread.sleep(500);
                } else {
                    Thread.sleep(3500);
                }
            } catch (InterruptedException e) {
                log.error("run", e);
            }

        }
    }

    static String utf16ToKS0066U(String utf16) {
        String ks0066u = "";
        char c;

        for (int i = 0; i < utf16.length(); i++) {
            int codePoint = utf16.codePointAt(i);

            if (Character.isHighSurrogate(utf16.charAt(i))) {
                // Skip low surrogate
                i++;
            }

            // ASCII subset from JIS X 0201
            if (codePoint >= 0x0020 && codePoint <= 0x007e) {
                // The LCD charset doesn't include '\' and '~', use similar characters instead
                switch (codePoint) {
                    case 0x005c:
                        c = (char) 0xa4;
                        break; // REVERSE SOLIDUS maps to IDEOGRAPHIC COMMA
                    case 0x007e:
                        c = (char) 0x2d;
                        break; // TILDE maps to HYPHEN-MINUS
                    default:
                        c = (char) codePoint;
                        break;
                }
            }
            // Katakana subset from JIS X 0201
            else if (codePoint >= 0xff61 && codePoint <= 0xff9f) {
                c = (char) (codePoint - 0xfec0);
            }
            // Special characters
            else {
                switch (codePoint) {
                    case 0x00a5:
                        c = (char) 0x5c;
                        break; // YEN SIGN
                    case 0x2192:
                        c = (char) 0x7e;
                        break; // RIGHTWARDS ARROW
                    case 0x2190:
                        c = (char) 0x7f;
                        break; // LEFTWARDS ARROW
                    case 0x00b0:
                        c = (char) 0xdf;
                        break; // DEGREE SIGN maps to KATAKANA SEMI-VOICED SOUND MARK
                    case 0x03b1:
                        c = (char) 0xe0;
                        break; // GREEK SMALL LETTER ALPHA
                    case 0x00c4:
                        c = (char) 0xe1;
                        break; // LATIN CAPITAL LETTER A WITH DIAERESIS
                    case 0x00e4:
                        c = (char) 0xe1;
                        break; // LATIN SMALL LETTER A WITH DIAERESIS
                    case 0x00df:
                        c = (char) 0xe2;
                        break; // LATIN SMALL LETTER SHARP S
                    case 0x03b5:
                        c = (char) 0xe3;
                        break; // GREEK SMALL LETTER EPSILON
                    case 0x00b5:
                        c = (char) 0xe4;
                        break; // MICRO SIGN
                    case 0x03bc:
                        c = (char) 0xe4;
                        break; // GREEK SMALL LETTER MU
                    case 0x03c2:
                        c = (char) 0xe5;
                        break; // GREEK SMALL LETTER FINAL SIGMA
                    case 0x03c1:
                        c = (char) 0xe6;
                        break; // GREEK SMALL LETTER RHO
                    case 0x221a:
                        c = (char) 0xe8;
                        break; // SQUARE ROOT
                    case 0x00b9:
                        c = (char) 0xe9;
                        break; // SUPERSCRIPT ONE maps to SUPERSCRIPT (minus) ONE
                    case 0x00a4:
                        c = (char) 0xeb;
                        break; // CURRENCY SIGN
                    case 0x00a2:
                        c = (char) 0xec;
                        break; // CENT SIGN
                    case 0x2c60:
                        c = (char) 0xed;
                        break; // LATIN CAPITAL LETTER L WITH DOUBLE BAR
                    case 0x00f1:
                        c = (char) 0xee;
                        break; // LATIN SMALL LETTER N WITH TILDE
                    case 0x00d6:
                        c = (char) 0xef;
                        break; // LATIN CAPITAL LETTER O WITH DIAERESIS
                    case 0x00f6:
                        c = (char) 0xef;
                        break; // LATIN SMALL LETTER O WITH DIAERESIS
                    case 0x03f4:
                        c = (char) 0xf2;
                        break; // GREEK CAPITAL THETA SYMBOL
                    case 0x221e:
                        c = (char) 0xf3;
                        break; // INFINITY
                    case 0x03a9:
                        c = (char) 0xf4;
                        break; // GREEK CAPITAL LETTER OMEGA
                    case 0x00dc:
                        c = (char) 0xf5;
                        break; // LATIN CAPITAL LETTER U WITH DIAERESIS
                    case 0x00fc:
                        c = (char) 0xf5;
                        break; // LATIN SMALL LETTER U WITH DIAERESIS
                    case 0x03a3:
                        c = (char) 0xf6;
                        break; // GREEK CAPITAL LETTER SIGMA
                    case 0x03c0:
                        c = (char) 0xf7;
                        break; // GREEK SMALL LETTER PI
                    case 0x0304:
                        c = (char) 0xf8;
                        break; // COMBINING MACRON
                    case 0x00f7:
                        c = (char) 0xfd;
                        break; // DIVISION SIGN

                    default:
                    case 0x25a0:
                        c = (char) 0xff;
                        break; // BLACK SQUARE
                }
            }

            // Special handling for 'x' followed by COMBINING MACRON
            if (c == (char) 0xf8) {
                if (!ks0066u.endsWith("x")) {
                    c = (char) 0xff; // BLACK SQUARE
                }

                if (ks0066u.length() > 0) {
                    ks0066u = ks0066u.substring(0, ks0066u.length() - 1);
                }
            }

            ks0066u += c;
        }

        return ks0066u;
    }

}
