package ru.goodloot.tr.markets;

import ru.goodloot.tr.utils.LoggerUtils;
import ru.goodloot.tr.utils.Utils;

/**
 * Created by artur on 18.12.14.
 */
public class PeriodicalInfoReceiver implements Runnable {

    private final int period;

    private volatile boolean runned = false;

    protected final MarketInfoReceiver marketInfoReceiver;

    public PeriodicalInfoReceiver(MarketInfoReceiver marketInfoReceiver,
            int period) {
        this.marketInfoReceiver = marketInfoReceiver;
        this.period = period;
    }

    @Override
    public void run() {

        while (true) {

            long timePassed = System.currentTimeMillis();

            try {

                marketInfoReceiver.getInfo();

            } catch (RuntimeException e) {
                LoggerUtils.out("Exception perfoming marketInfoReceiver "
                        + marketInfoReceiver.getClass().getSimpleName());
                e.printStackTrace();
            }

            timePassed = System.currentTimeMillis() - timePassed;

            if (timePassed < period) {
                Utils.sleep(period - timePassed);
            }
        }
    }

    public void startThread() {

        if (!runned) {
            runned = true;
            new Thread(this).start();
        } else {
            throw new RuntimeException("That thread already started "
                    + marketInfoReceiver.getClass().getSimpleName());
        }
    }

    public boolean isRunned() {
        return runned;
    }

}
