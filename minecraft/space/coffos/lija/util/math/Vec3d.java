package space.coffos.lija.util.math;

import net.minecraft.util.MathHelper;

/**
 * @author Zhn17
 * <-> 2018-04-21 <-> 12:23
 **/
public class Vec3d {

    public final double x;
    public final double y;
    public final double z;

    private Vec3d(double x, double y, double z) {
        if (x == -0.0D) x = 0.0D;
        if (y == -0.0D) y = 0.0D;
        if (z == -0.0D) z = 0.0D;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3d scale(double p_186678_1_) {
        return new Vec3d(this.x * p_186678_1_, this.y * p_186678_1_, this.z * p_186678_1_);
    }

    public Vec3d normalize() {
        double var1 = MathHelper.sqrt_double(this.x * this.x + this.y * this.y + this.z * this.z);
        return var1 < 1.0E-4D ? new Vec3d(0.0D, 0.0D, 0.0D) : new Vec3d(this.x / var1, this.y / var1, this.z / var1);
    }

    public Vec3d subtract(Vec3d p_178788_1_) {
        return subtract(p_178788_1_.x, p_178788_1_.y, p_178788_1_.z);
    }

    public Vec3d subtract(double p_178786_1_, double p_178786_3_, double p_178786_5_) {
        return addVector(-p_178786_1_, -p_178786_3_, -p_178786_5_);
    }

    public Vec3d add(Vec3d p_178787_1_) {
        return addVector(p_178787_1_.x, p_178787_1_.y, p_178787_1_.z);
    }

    public Vec3d addVector(double x, double y, double z) {
        return new Vec3d(this.x + x, this.y + y, this.z + z);
    }

    public double distanceTo(Vec3d vec) {
        double var2 = vec.x - this.x;
        double var4 = vec.y - this.y;
        double var6 = vec.z - this.z;
        return MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }
}