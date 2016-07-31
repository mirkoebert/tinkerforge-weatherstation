# tinkerforge-weatherstation
Simple weather station software for tinkerfoge weather station kit.
More Information, see http://programming-2.blogspot.de/2016/07/tinkerforge-weather-station-published.html



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

or


```bash
java -jar target/tinkerforge-weatherstation-0.0.2-SNAPSHOT-jar-with-dependencies.jar 
```
If a sensor value has changed, the new value is printed out to console and the display of the weather station is updated.


Stop the program with `<crtl><c>`

