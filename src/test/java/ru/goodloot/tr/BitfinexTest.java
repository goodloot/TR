package ru.goodloot.tr;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import ru.goodloot.tr.markets.tickers.BitfinexTicker;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author lol
 */
public class BitfinexTest {

    public BitfinexTest() {
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
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //

    @Test
    public void testTicker() {
        BitfinexTicker t = new BitfinexTicker();
        t.setTicker();
    }
}
