package space.coffos.lija.impl.events;

import space.coffos.lija.api.event.Event;

public class EventSlowDown extends Event {

    public EventSlowDown() {
        super(Type.SINGLE);
    }
}