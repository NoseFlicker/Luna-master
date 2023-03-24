package space.coffos.lija.impl.events;

import space.coffos.lija.api.event.Event;

/**
 * @author Zhn17
 * <-> 2018-08-20 <->
 * space.lunaclient.luna.impl.events
 **/
public class EventStep extends Event {

    private boolean active, pre;

    public void fire(boolean state, double stepHeight, double realHeight) {
        this.pre = state;
        super.call();
    }

    public void fire(boolean state, double stepHeight) {
        this.pre = state;
        super.call();
    }

    public boolean isPre() {
        return this.pre;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean bypass) {
        this.active = bypass;
    }

}