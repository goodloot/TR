package ru.goodloot.tr.markets.data;

import ru.goodloot.tr.utils.LoggerUtils;

/**
 * @author Artur M.
 * @created Oct 15, 2014
 * 
 * @Description ...
 */
public class PAPriceValue {

    public PAPriceValue() {}

    public PAPriceValue(PAPriceValue price) {

        master.setBuy(price.getMaster().getBuy());
        master.setSell(price.getMaster().getSell());
        slave.setBuy(price.getSlave().getBuy());
        slave.setSell(price.getSlave().getSell());
    }

    public void setPrice(PAPriceValue price) {

        master.setBuy(price.getMaster().getBuy());
        master.setSell(price.getMaster().getSell());
        slave.setBuy(price.getSlave().getBuy());
        slave.setSell(price.getSlave().getSell());
    }

    private PriceValue master = new PriceValue();

    private PriceValue slave = new PriceValue();

    public boolean equals(PAPriceValue i) {
        return i.getMaster().equals(master) && i.getSlave().equals(slave);
    }

    @Override
    public String toString() {
        return LoggerUtils.getFullStrWithoutDate(master, slave);
    }

    public PriceValue getMaster() {
        return master;
    }

    public void setMaster(PriceValue master) {
        this.master = master;
    }

    public PriceValue getSlave() {
        return slave;
    }

    public void setSlave(PriceValue slave) {
        this.slave = slave;
    }
}
