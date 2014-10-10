package ru.goodloot.tr;

import org.junit.Test;
import ru.goodloot.tr.markets.pabots.PAAnx;
import ru.goodloot.tr.markets.tickers.BitfinexTicker;

/**
 * @author Artur M.
 * @created Oct 10, 2014
 * 
 * @Description ...
 */
public class UtilsTest {

    @Test
    public void testReadLast() {
        //        System.out.println(new File(".").getAbsolutePath());

        PAAnx anx = new PAAnx(null, new TickerThread(new BitfinexTicker()), null);
        //        anx.initLastsRatio("target/test-classes");
        //        System.out.println(LoggerUtils.readLast("target/test-classes/buy.txt"));
    }
}
