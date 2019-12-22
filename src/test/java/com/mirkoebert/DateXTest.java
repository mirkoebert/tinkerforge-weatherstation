package com.mirkoebert;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DateXTest {

    DateX dx = DateX.getInstance();
    
    @Test
    void testGetDateString() {
        assertNotNull(dx.getDateString());
    }

    @Test
    void testIsOnline() {
        assertTrue(dx.isOnline());
    }

    @Test
    void testGetInstance() {      
        assertNotNull(dx);
    }

    @Test
    void testGetDateOnlyString() {
        assertNotNull(dx.getDateOnlyString());
    }

    @Test
    void testGetTimeOnlyString() {
        assertNotNull(dx.getTimeOnlyString());
    }

}
