package net.threetag.threecore.scripts.accessors;

import net.minecraft.util.math.Vec3d;
import net.threetag.threecore.scripts.ScriptParameterName;

public class Vec3dAccessor extends ScriptAccessor<Vec3d> {

    public Vec3dAccessor(Vec3d value) {
        super(value);
    }

    public double getX() {
        return this.value.getX();
    }

    public double getY() {
        return this.value.getY();
    }

    public double getZ() {
        return this.value.getZ();
    }

    public Vec3dAccessor subtractReverse(@ScriptParameterName("vector") Vec3dAccessor vector) {
        return new Vec3dAccessor(this.value.subtractReverse(vector.value));
    }

    public Vec3dAccessor normalize() {
        return new Vec3dAccessor(this.value.normalize());
    }

    public double dotProduct(@ScriptParameterName("vector") Vec3dAccessor vector) {
        return this.value.dotProduct(vector.value);
    }

    public Vec3dAccessor crossProduct(@ScriptParameterName("vector") Vec3dAccessor vector) {
        return new Vec3dAccessor(this.value.crossProduct(vector.value));
    }

    public Vec3dAccessor subtract(@ScriptParameterName("vector") Vec3dAccessor vector) {
        return new Vec3dAccessor(this.value.subtract(vector.value));
    }

    public Vec3dAccessor subtract(@ScriptParameterName("x") double x, @ScriptParameterName("y") double y, @ScriptParameterName("z") double z) {
        return new Vec3dAccessor(this.value.subtract(x, y, z));
    }

    public Vec3dAccessor add(@ScriptParameterName("vector") Vec3dAccessor vector) {
        return new Vec3dAccessor(this.value.add(vector.value));
    }

    public Vec3dAccessor add(@ScriptParameterName("x") double x, @ScriptParameterName("y") double y, @ScriptParameterName("z") double z) {
        return new Vec3dAccessor(this.value.add(x, y, z));
    }

    public double distanceTo(@ScriptParameterName("vector") Vec3dAccessor vector) {
        return this.value.distanceTo(vector.value);
    }

    public double squareDistanceTo(@ScriptParameterName("vector") Vec3dAccessor vector) {
        return this.value.squareDistanceTo(vector.value);
    }

    public double squareDistanceTo(@ScriptParameterName("x") double x, @ScriptParameterName("y") double y, @ScriptParameterName("z") double z) {
        return this.value.squareDistanceTo(x, y, z);
    }

    public Vec3dAccessor scale(@ScriptParameterName("scale") double scale) {
        return new Vec3dAccessor(this.value.scale(scale));
    }

    public Vec3dAccessor inverse() {
        return new Vec3dAccessor(this.value.inverse());
    }

    public Vec3dAccessor mul(@ScriptParameterName("vector") Vec3dAccessor vector) {
        return new Vec3dAccessor(this.value.mul(vector.value));
    }

    public Vec3dAccessor mul(@ScriptParameterName("factorX") double factorX, @ScriptParameterName("factorY") double factorY, @ScriptParameterName("factorZ") double factorZ) {
        return new Vec3dAccessor(this.value.mul(factorX, factorY, factorZ));
    }

    public double length() {
        return this.value.length();
    }

    public double lengthSquared() {
        return this.value.lengthSquared();
    }

    public Vec3dAccessor rotatePitch(@ScriptParameterName("pitch") float pitch) {
        return new Vec3dAccessor(this.value.rotatePitch(pitch));
    }

    public Vec3dAccessor rotateYaw(@ScriptParameterName("yaw") float yaw) {
        return new Vec3dAccessor(this.value.rotateYaw(yaw));
    }

}
