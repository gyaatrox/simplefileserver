package com.gy.fileserver;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    private static Logger log = LoggerFactory.getLogger(AppTest.class);
    @Test
    public void shouldAnswerWithTrue() {
        log.debug("hello");
        log.info("info");
        System.out.println("hello");
        assertTrue( true );
    }

    @Test
    public void test(){
        Properties prop = new Properties();
        try {
            prop.load(new FileReader("src/main/resources/publicKey.properties"));
            System.out.println(prop.getProperty("publicKey"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
