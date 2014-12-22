package ru.goodloot.tr;

import org.junit.Test;
import ru.goodloot.tr.markets.depth.AnxDepth;
import ru.goodloot.tr.markets.exchange.Anx;
import ru.goodloot.tr.utils.Utils;

/**
 * Created by artur on 22.12.14.
 */
public class AnxDepthTest {

    @Test
    public void test() {

        AnxDepth depth = new AnxDepth();
        depth.getInfo();
    }

    @Test
    public void testTime() {

        long depthTotal = 0;
        long tickerTotal = 0;

        for (int i = 0; i < 10; i++) {

            long depth = System.currentTimeMillis();
            Anx.callFunc("btcusd", "depth/full");
            depthTotal += System.currentTimeMillis() - depth;

            Utils.sleep(1000);

            long ticker = System.currentTimeMillis();
            Anx.callFunc("btcusd", "ticker");
            tickerTotal += System.currentTimeMillis() - ticker;

            Utils.sleep(1000);
        }

        System.out.println("Depth: " + depthTotal);
        System.out.println("Ticker: " + tickerTotal);
    }
}
