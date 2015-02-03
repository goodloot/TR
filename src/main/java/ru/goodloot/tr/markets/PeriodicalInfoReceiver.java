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

//    private static final Logger logger = LoggerFactory.getLogger(PeriodicalInfoReceiver.class);

    public PeriodicalInfoReceiver(MarketInfoReceiver marketInfoReceiver,
            int period) {
        this.marketInfoReceiver = marketInfoReceiver;
        this.period = period;
    }

    @Override
    public void run() {

        while (true) {

            long timePassed = System.currentTimeMillis();

            Thread infoThread = new Thread(() ->  {
                try {
                    marketInfoReceiver.getInfo();
                } catch (RuntimeException e) {
                    LoggerUtils.out("Exception performing marketInfoReceiver "
                            + marketInfoReceiver.getClass().getSimpleName(), e.getMessage());
//                    e.printStackTrace();
                }
            });
            infoThread.start();

            try {
                infoThread.join(10 * 1000);
            } catch (InterruptedException e) {
                LoggerUtils.out("Interrupted exception");
                e.printStackTrace();
            }

            if (infoThread.isAlive()) {
                LoggerUtils
                        .out("Interrupting getInfo thread after 10 seconds",
                                infoThread.getStackTrace());
                infoThread.interrupt();
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
