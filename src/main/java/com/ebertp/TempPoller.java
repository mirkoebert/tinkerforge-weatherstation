package com.ebertp;

import java.util.Timer;
import java.util.TimerTask;

import com.tinkerforge.BrickMaster;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import lombok.extern.slf4j.Slf4j;

/**
 * Polls the master brick chip temp. The value is much higher then the real temp. It's better to use the chip temp from air pressure bricklet.
 * @author mirkoebert
 *
 */
@Slf4j
public class TempPoller extends TimerTask{

    final BrickMaster master;
    
    public TempPoller(BrickMaster master) {
        this.master = master;
        new Timer().schedule(this, 10, 60000);
    }

    @Override
    public void run() {
        try {
            short temp = master.getChipTemperature();
            log.info("Temp chip master: "+temp);
        } catch (TimeoutException | NotConnectedException e) {
            log.error(e.getLocalizedMessage());
        }
        
    }
    
    
    

}