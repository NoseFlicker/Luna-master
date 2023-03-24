package space.coffos.lija.util.math;

import net.minecraft.util.Vec3;

/**
 * @author Trol
 * <-> 2018-04-26 <-> 12:12
 **/
public class Vector3DUtils {

    private Vec3 vec3;

    public Vector3DUtils(Vec3 vector, float yaw, float pitch, double length) {
        double calculatedX = Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
        double calculatedY = Math.sin(Math.toRadians(pitch));
        double calculatedZ = Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
        double x = calculatedX * length + vector.xCoord;
        double y = calculatedY * length + vector.yCoord;
        double z = calculatedZ * length + vector.zCoord;
        vec3 = new Vec3(x, y, z);
    }

    public Vec3 getEndVector() {
        return vec3;
    }
}