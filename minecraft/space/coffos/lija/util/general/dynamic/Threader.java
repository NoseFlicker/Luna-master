package space.coffos.lija.util.general.dynamic;

public class Threader {

    /**
     * Create a new thread and run it.
     */
    public static void newThread(Runnable exec) {
        new Thread(exec).start();
    }
}