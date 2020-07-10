package net.threetag.threecore.scripts.accessors;

import net.minecraft.util.math.vector.Vector3d;
import net.threetag.threecore.scripts.ScriptParameterName;

public class Vector3dAccessor extends ScriptAccessor<Vector3d> {

    public Vector3dAccessor(Vector3d value) {
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

    public Vector3dAccessor subtractReverse(@ScriptParameterName("vector") Vector3dAccessor vector) {
        return new Vector3dAccessor(this.value.subtractReverse(vector.value));
    }

    public Vector3dAccessor normalize() {
        return new Vector3dAccessor(this.value.normalize());
    }

    public double dotProduct(@ScriptParameterName("vector") Vector3dAccessor vector) {
        return this.value.dotProduct(vector.value);
    }

    public Vector3dAccessor crossProduct(@ScriptParameterName("vector") Vector3dAccessor vector) {
        return new Vector3dAccessor(this.value.crossProduct(vector.value));
    }

    public Vector3dAccessor subtract(@ScriptParameterName("vector") Vector3dAccessor vector) {
        return new Vector3dAccessor(this.value.subtract(vector.value));
    }

    public Vector3dAccessor subtract(@ScriptParameterName("x") double x, @ScriptParameterName("y") double y, @ScriptParameterName("z") double z) {
        return new Vector3dAccessor(this.value.subtract(x, y, z));
    }

    public Vector3dAccessor add(@ScriptParameterName("vector") Vector3dAccessor vector) {
        return new Vector3dAccessor(this.value.add(vector.value));
    }

    public Vector3dAccessor add(@ScriptParameterName("x") double x, @ScriptParameterName("y") double y, @ScriptParameterName("z") double z) {
        return new Vector3dAccessor(this.value.add(x, y, z));
    }

    public double distanceTo(@ScriptParameterName("vector") Vector3dAccessor vector) {
        return this.value.distanceTo(vector.value);
    }

    public double squareDistanceTo(@ScriptParameterName("vector") Vector3dAccessor vector) {
        return this.value.squareDistanceTo(vector.value);
    }

    public double squareDistanceTo(@ScriptParameterName("x") double x, @ScriptParameterName("y") double y, @ScriptParameterName("z") double z) {
        return this.value.squareDistanceTo(x, y, z);
    }

    public Vector3dAccessor scale(@ScriptParameterName("scale") double scale) {
        return new Vector3dAccessor(this.value.scale(scale));
    }

    public Vector3dAccessor inverse() {
        return new Vector3dAccessor(this.value.inverse());
    }

    public Vector3dAccessor mul(@ScriptParameterName("vector") Vector3dAccessor vector) {
        return new Vector3dAccessor(this.value.mul(vector.value));
    }

    public Vector3dAccessor mul(@ScriptParameterName("factorX") double factorX, @ScriptParameterName("factorY") double factorY, @ScriptParameterName("factorZ") double factorZ) {
        return new Vector3dAccessor(this.value.mul(factorX, factorY, factorZ));
    }

    public double length() {
        return this.value.length();
    }

    public double lengthSquared() {
        return this.value.lengthSquared();
    }

    public Vector3dAccessor rotatePitch(@ScriptParameterName("pitch") float pitch) {
        return new Vector3dAccessor(this.value.rotatePitch(pitch));
    }

    public Vector3dAccessor rotateYaw(@ScriptParameterName("yaw") float yaw) {
        return new Vector3dAccessor(this.value.rotateYaw(yaw));
    }

}
