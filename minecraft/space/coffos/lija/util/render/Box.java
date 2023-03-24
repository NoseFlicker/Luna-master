package space.coffos.lija.util.render;

/*
 * @author Direkt Client
 */
public final class Box {

    public final double minX, minY, minZ, maxX, maxY, maxZ;

    public Box(double x, double y, double z, double x1, double y1, double z1) {
        minX = x;
        minY = y;
        minZ = z;
        maxX = x1;
        maxY = y1;
        maxZ = z1;
    }
}