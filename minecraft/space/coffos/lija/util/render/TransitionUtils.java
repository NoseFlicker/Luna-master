package space.coffos.lija.util.render;

public class TransitionUtils {

    private static int transitionIN;
    private static float transitionFL;

    public static Object getTransition(boolean floatTransition) {
        return floatTransition ? transitionFL : transitionIN;
    }

    public static int transitionTo(int start, int end, int speed) {
        if (transitionIN < start) transitionIN = start;
        if (transitionIN > end) transitionIN = end;
        if (transitionIN != end) transitionIN += speed;
        return transitionIN;
    }

    public static float transitionTo(float start, float end, float speed) {
        if (transitionFL < start) transitionFL = start;
        if (transitionFL > end) transitionFL = end;
        if (transitionFL != end) transitionFL += speed;
        return transitionFL;
    }

    public static void resetTransition(boolean floatTransition) {
        if (floatTransition) transitionFL = 0;
        else transitionIN = 0;
    }
}