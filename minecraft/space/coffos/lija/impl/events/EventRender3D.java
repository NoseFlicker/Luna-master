package space.coffos.lija.impl.events;

import space.coffos.lija.api.event.Event;

public class EventRender3D extends Event {
    private float partialTicks;

    public EventRender3D(float partialTicks) {
        super(Type.SINGLE);
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}