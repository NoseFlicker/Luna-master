package space.coffos.lija.impl.events;

import space.coffos.lija.api.event.Event;

public class EventKeyBoard extends Event {

    private int key;

    public EventKeyBoard(int key) {
        super(Type.SINGLE);
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}