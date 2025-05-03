package keystrokesmod.minecraft;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;

public class Vec3 extends Vec3d {
    public final double xCoord;
    public final double yCoord;
    public final double zCoord;

    public Vec3(double p_i1108_1_, double p_i1108_3_, double p_i1108_5_) {
        super(p_i1108_1_, p_i1108_3_, p_i1108_5_);
        if (p_i1108_1_ == -0.0) {
            p_i1108_1_ = 0.0;
        }

        if (p_i1108_3_ == -0.0) {
            p_i1108_3_ = 0.0;
        }

        if (p_i1108_5_ == -0.0) {
            p_i1108_5_ = 0.0;
        }

        this.xCoord = p_i1108_1_;
        this.yCoord = p_i1108_3_;
        this.zCoord = p_i1108_5_;
    }

    public Vec3(Vec3i p_i46377_1_) {
        this(p_i46377_1_.getX(), p_i46377_1_.getY(), p_i46377_1_.getZ());
    }

    public Vec3 subtractReverse(Vec3 p_subtractReverse_1_) {
        return new Vec3(p_subtractReverse_1_.xCoord - this.xCoord, p_subtractReverse_1_.yCoord - this.yCoord, p_subtractReverse_1_.zCoord - this.zCoord);
    }

    public @NotNull Vec3 normalize() {
        double d0 = MathHelper.sqrt(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
        return d0 < 1.0E-4 ? new Vec3(0.0, 0.0, 0.0) : new Vec3(this.xCoord / d0, this.yCoord / d0, this.zCoord / d0);
    }

    public double dotProduct(Vec3 p_dotProduct_1_) {
        return this.xCoord * p_dotProduct_1_.xCoord + this.yCoord * p_dotProduct_1_.yCoord + this.zCoord * p_dotProduct_1_.zCoord;
    }

    public Vec3 crossProduct(Vec3 p_crossProduct_1_) {
        return new Vec3(this.yCoord * p_crossProduct_1_.zCoord - this.zCoord * p_crossProduct_1_.yCoord, this.zCoord * p_crossProduct_1_.xCoord - this.xCoord * p_crossProduct_1_.zCoord, this.xCoord * p_crossProduct_1_.yCoord - this.yCoord * p_crossProduct_1_.xCoord);
    }

    public Vec3 subtract(Vec3 p_subtract_1_) {
        return this.subtract(p_subtract_1_.xCoord, p_subtract_1_.yCoord, p_subtract_1_.zCoord);
    }

    public @NotNull Vec3 subtract(double p_subtract_1_, double p_subtract_3_, double p_subtract_5_) {
        return this.addVector(-p_subtract_1_, -p_subtract_3_, -p_subtract_5_);
    }

    public Vec3 add(Vec3 p_add_1_) {
        return this.addVector(p_add_1_.xCoord, p_add_1_.yCoord, p_add_1_.zCoord);
    }

    public @NotNull Vec3 addVector(double p_addVector_1_, double p_addVector_3_, double p_addVector_5_) {
        return new Vec3(this.xCoord + p_addVector_1_, this.yCoord + p_addVector_3_, this.zCoord + p_addVector_5_);
    }

    public double distanceTo(Vec3 p_distanceTo_1_) {
        double d0 = p_distanceTo_1_.xCoord - this.xCoord;
        double d1 = p_distanceTo_1_.yCoord - this.yCoord;
        double d2 = p_distanceTo_1_.zCoord - this.zCoord;
        return MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    public double squareDistanceTo(Vec3 p_squareDistanceTo_1_) {
        double d0 = p_squareDistanceTo_1_.xCoord - this.xCoord;
        double d1 = p_squareDistanceTo_1_.yCoord - this.yCoord;
        double d2 = p_squareDistanceTo_1_.zCoord - this.zCoord;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public double lengthVector() {
        return MathHelper.sqrt(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
    }

    public Vec3 getIntermediateWithXValue(Vec3 p_getIntermediateWithXValue_1_, double p_getIntermediateWithXValue_2_) {
        double d0 = p_getIntermediateWithXValue_1_.xCoord - this.xCoord;
        double d1 = p_getIntermediateWithXValue_1_.yCoord - this.yCoord;
        double d2 = p_getIntermediateWithXValue_1_.zCoord - this.zCoord;
        if (d0 * d0 < 1.0000000116860974E-7) {
            return null;
        } else {
            double d3 = (p_getIntermediateWithXValue_2_ - this.xCoord) / d0;
            return d3 >= 0.0 && d3 <= 1.0 ? new Vec3(this.xCoord + d0 * d3, this.yCoord + d1 * d3, this.zCoord + d2 * d3) : null;
        }
    }

    public Vec3 getIntermediateWithYValue(Vec3 p_getIntermediateWithYValue_1_, double p_getIntermediateWithYValue_2_) {
        double d0 = p_getIntermediateWithYValue_1_.xCoord - this.xCoord;
        double d1 = p_getIntermediateWithYValue_1_.yCoord - this.yCoord;
        double d2 = p_getIntermediateWithYValue_1_.zCoord - this.zCoord;
        if (d1 * d1 < 1.0000000116860974E-7) {
            return null;
        } else {
            double d3 = (p_getIntermediateWithYValue_2_ - this.yCoord) / d1;
            return d3 >= 0.0 && d3 <= 1.0 ? new Vec3(this.xCoord + d0 * d3, this.yCoord + d1 * d3, this.zCoord + d2 * d3) : null;
        }
    }

    public Vec3 getIntermediateWithZValue(Vec3 p_getIntermediateWithZValue_1_, double p_getIntermediateWithZValue_2_) {
        double d0 = p_getIntermediateWithZValue_1_.xCoord - this.xCoord;
        double d1 = p_getIntermediateWithZValue_1_.yCoord - this.yCoord;
        double d2 = p_getIntermediateWithZValue_1_.zCoord - this.zCoord;
        if (d2 * d2 < 1.0000000116860974E-7) {
            return null;
        } else {
            double d3 = (p_getIntermediateWithZValue_2_ - this.zCoord) / d2;
            return d3 >= 0.0 && d3 <= 1.0 ? new Vec3(this.xCoord + d0 * d3, this.yCoord + d1 * d3, this.zCoord + d2 * d3) : null;
        }
    }

    public @NotNull String toString() {
        return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
    }

    public @NotNull Vec3 rotatePitch(float p_rotatePitch_1_) {
        float f = MathHelper.cos(p_rotatePitch_1_);
        float f1 = MathHelper.sin(p_rotatePitch_1_);
        double d1 = this.yCoord * (double)f + this.zCoord * (double)f1;
        double d2 = this.zCoord * (double)f - this.yCoord * (double)f1;
        return new Vec3(this.xCoord, d1, d2);
    }

    public @NotNull Vec3 rotateYaw(float p_rotateYaw_1_) {
        float f = MathHelper.cos(p_rotateYaw_1_);
        float f1 = MathHelper.sin(p_rotateYaw_1_);
        double d0 = this.xCoord * (double)f + this.zCoord * (double)f1;
        double d2 = this.zCoord * (double)f - this.xCoord * (double)f1;
        return new Vec3(d0, this.yCoord, d2);
    }
}

