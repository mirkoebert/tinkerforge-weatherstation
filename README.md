# tinkerforge-weatherstation
Simple weather station software for Tinkerfoge weather station kit.
More Information, see 
* http://programming-2.blogspot.de/2016/07/tinkerforge-weather-station-published.html
* http://www.tinkerforge.com/de/doc/Kits/WeatherStation/WeatherStation.html#starter-kit-weather-station

## Requirements
* Java 8 JDK
* Maven 

## Build
```bash
mvn clean package
```
It compiles the code and build two packages one with all depedencies and without.
See folder `target`.

## Run
```bash
mvn exec:java -Dexec.mainClass="com.ebertp.WeatherStation"
```

**or**


```bash
java -jar target/tinkerforge-weatherstation-0.0.2-SNAPSHOT-jar-with-dependencies.jar 
```
If a sensor value has changed, the new value is printed out to console and the display of the weather station is updated.


Stop the program with `<crtl><c>`


**or** 

Execute on RED Brick http://www.tinkerforge.com/de/doc/Hardware/Bricks/RED_Brick.html


Please make sure that the RED Brick is under your master brick. If not you've got a Java Exception like  
```
Exception in thread "main" com.tinkerforge.TimeoutException: Did not receive response in time for function ID 3
```

## Features
* Multithreaded with Call Backs
* Measures 
  * Ambient Light in lux
  * Air pressure in hPa
  * Relative Humidity in %
  * Temperature (only as chip temperature) in degree Celsius.
* Date and Time
* Warnings
  * Humidity to low or to high
  * Frost damage
