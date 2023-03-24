package space.coffos.lija.util.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class MathUtils {

    private static final Random rng = new Random();

    public static double roundToPlace(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static int getRandom(int max) {
        return rng.nextInt(max);
    }

    public static double setRandom(double min, double max) {
        Random random = new Random();
        return min + random.nextDouble() * (max - min);
    }
}