package com.mirkoebert;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class MainTest {

    @Test
    public void contextLoads() {
        // test loading full spring context
    }

}
