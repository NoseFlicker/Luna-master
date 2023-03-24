package space.coffos.lija.impl.events;

import space.coffos.lija.api.event.Event;

public class EventMotion extends Event {

    private float yaw, pitch;
    private boolean ground;
    private double y;

    public EventMotion(Type type, float yaw, float pitch, boolean ground, double y) {
        super(type);
        this.yaw = yaw;
        this.pitch = pitch;
        this.ground = ground;
        this.y = y;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public boolean isGround() {
        return ground;
    }

    public void setGround(boolean ground) {
        this.ground = ground;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}