package space.coffos.lija.util.entity;

import space.coffos.lija.LiJA;
import space.coffos.lija.util.entity.list.Booster;
import space.coffos.lija.util.packets.PacketUtils;

public class MoveUtils {

    private static Booster b = new Booster();
    public static boolean validated, scf;

    public static void motionBoost(double boost, double fade) {
        validated = true;
        Booster.slowDown = fade;
        Booster.boost = boost;
        LiJA.INSTANCE.eventManager.register(b);
    }

    public static void motionBoostScaffold(double boost, double fade, boolean scff) {
        validated = true;
        scf = scff;
        Booster.slowDown = fade;
        Booster.boost = boost;
        LiJA.INSTANCE.eventManager.register(b);
    }

    public static void disableBoost() {
        validated = false;
        PacketUtils.setState(false);
        LiJA.INSTANCE.eventManager.unregister(b);
    }
}