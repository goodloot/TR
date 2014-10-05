package ru.goodloot.tr;

/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
import ru.goodloot.tr.markets.tickers.BitstampTicker;
import ru.goodloot.tr.utils.Utils;
import ru.goodloot.tr.utils.Logger;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author lol
 */
public class TradeBotTest {

    /**
     *
     */
    @Test
    public void TradeBotTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDate() {

        String d = new Date().toString();
        System.out.println(d);
    }

    @Test
    public void testLoggerNew() {

        Logger logger = new Logger("test");
        double num = 0.01;
        logger.out("123", 1.0 / 3, num, "asd", 100);
        logger.write("logNew.txt", Utils.getNonce() / 1000, 100, 213, 132, 123);
    }

    @Test
    public void testMasterName() {

        BitstampTicker bitstampTicker = new BitstampTicker();
        // PAKraken<BitstampTicker> paKrakenStamp = new
        // PAKraken("krakenStamp.conf", bitstampTicker);
        // System.out.println(paKrakenStamp.getFolder());
    }
}
