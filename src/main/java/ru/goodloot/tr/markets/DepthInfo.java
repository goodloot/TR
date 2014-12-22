package ru.goodloot.tr.markets;

import ru.goodloot.tr.markets.depth.AbstractDepth;

/**
 * Created by artur on 22.12.14.
 */
public class DepthInfo extends PeriodicalInfoReceiver {

    public DepthInfo(AbstractDepth abstractDepth) {
        super(abstractDepth, 1000);
    }
}
