package space.coffos.lija.impl.events;

import space.coffos.lija.api.event.Event;

public class EventLateUpdate extends Event {

    public EventLateUpdate() {
        super(Type.SINGLE);
    }
}