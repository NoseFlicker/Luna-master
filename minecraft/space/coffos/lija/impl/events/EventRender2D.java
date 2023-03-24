package space.coffos.lija.impl.events;

import space.coffos.lija.api.event.Event;

public class EventRender2D extends Event {

    private int width, height;

    public EventRender2D(int width, int height) {
        super(Type.SINGLE);
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}