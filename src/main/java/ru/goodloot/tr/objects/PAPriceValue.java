package ru.goodloot.tr.objects;

import ru.goodloot.tr.utils.LoggerUtils;

/**
 * @author Artur M.
 * @created Oct 15, 2014
 * 
 * @Description ...
 */
public class PAPriceValue {

    private PriceValue master;

    private PriceValue slave;

    public boolean isSame(PAPriceValue i) {
        return i.getMaster().isSame(master) && i.getSlave().isSame(slave);
    }

    public void setPrice(PAPriceValue i) {

        master = i.getMaster();
        slave = i.getSlave();
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
