package ru.goodloot.tr;

import org.junit.Test;
import ru.goodloot.tr.markets.pabots.PAAnx;
import ru.goodloot.tr.markets.tickers.BitfinexTicker;
import ru.goodloot.tr.utils.LoggerUtils;

/**
 * @author Artur M.
 * @created Oct 10, 2014
 * 
 * @Description ...
 */
public class UtilsTest {

    @Test
    public void testReadLast() {
        // System.out.println(new File(".").getAbsolutePath());

        PAAnx anx = new PAAnx(null, new TickerThread(new BitfinexTicker()), null);
        // anx.initLastsRatio("target/test-classes");
        // System.out.println(LoggerUtils.readLast("target/test-classes/buy.txt"));
    }

    @Test
    public void testLogger() {

        LoggerUtils.out(1.0 / 3000, 2.0, 0.3333333333, 313.123123123123, 313.22);
    }
}
