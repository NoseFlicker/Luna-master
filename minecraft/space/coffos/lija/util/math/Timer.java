package space.coffos.lija.util.math;

public class Timer {

    private long lastMS;

    public Timer() {
        lastMS = getCurrentMS();
    }

    private long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasReached(long milliseconds) {
        return getCurrentMS() - lastMS >= milliseconds;
    }

    public long getTime() {
        return getCurrentMS() - lastMS;
    }

    public void reset() {
        lastMS = getCurrentMS();
    }
}