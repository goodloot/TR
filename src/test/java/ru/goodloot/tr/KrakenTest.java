package ru.goodloot.tr;

/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.goodloot.tr.markets.Kraken;

/**
 * 
 * @author lol
 */
public class KrakenTest {

    Kraken k;

    public KrakenTest() {}

    @BeforeClass
    public static void setUpClass() {}

    @AfterClass
    public static void tearDownClass() {}

    @Before
    public void setUp() {
        k = new Kraken("test", "test");
    }

    @After
    public void tearDown() {}

    @Test
    public void testKrakenBalance() {
        k.setFundsAmount();
    }

    @Test
    public void testKrakenFunc() {
        try {
            System.out.println(Kraken.callFunc("XBTUSD", "Ticker"));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void test() {
        try {
            Assert.assertTrue(k.tradeSellMargin(0.01));
            k.getOrderInfo();
            Assert.assertTrue(k.cancelLastOrder());
            // k.cancelLastOrder();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testSetFunds() {
        k.setFundsAmount();
        System.out.println(k.getBtcAmount());
        System.out.println(k.getUsdAmount());
    }
}
