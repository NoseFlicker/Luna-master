package space.coffos.lija.util.packets;

public class PacketUtils {

    public static double xC, yC, zC, cX, cY, cZ;
    public static boolean isOnGround, state;

    public static void interceptC03Pos(double x, double y, double z, boolean onGround) {
        xC = x;
        yC = y;
        zC = z;
        isOnGround = onGround;
    }

    public static void setState(boolean allow) {
        state = allow;
    }

    public static void reset() {
        xC = yC = zC = 0;
    }

    public static boolean getState() {
        return state;
    }

    public static double getX() {
        return cX;
    }

    public static double getY() {
        return cY;
    }

    public static double getZ() {
        return cZ;
    }
}