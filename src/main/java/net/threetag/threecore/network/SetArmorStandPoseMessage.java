package net.threetag.threecore.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Rotations;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SetArmorStandPoseMessage {

    private int entityId;
    private Rotations head;
    private Rotations body;
    private Rotations rightArm;
    private Rotations leftArm;
    private Rotations rightLeg;
    private Rotations leftLeg;

    public SetArmorStandPoseMessage(int entityId, Rotations head, Rotations body, Rotations rightArm, Rotations leftArm, Rotations rightLeg, Rotations leftLeg) {
        this.entityId = entityId;
        this.head = head;
        this.body = body;
        this.rightArm = rightArm;
        this.leftArm = leftArm;
        this.rightLeg = rightLeg;
        this.leftLeg = leftLeg;
    }

    public SetArmorStandPoseMessage(PacketBuffer buf) {
        this.entityId = buf.readInt();
        this.head = new Rotations(buf.readFloat(), buf.readFloat(), buf.readFloat());
        this.body = new Rotations(buf.readFloat(), buf.readFloat(), buf.readFloat());
        this.rightArm = new Rotations(buf.readFloat(), buf.readFloat(), buf.readFloat());
        this.leftArm = new Rotations(buf.readFloat(), buf.readFloat(), buf.readFloat());
        this.rightLeg = new Rotations(buf.readFloat(), buf.readFloat(), buf.readFloat());
        this.leftLeg = new Rotations(buf.readFloat(), buf.readFloat(), buf.readFloat());
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(this.entityId);
        buf.writeFloat(this.head.getX());
        buf.writeFloat(this.head.getY());
        buf.writeFloat(this.head.getZ());

        buf.writeFloat(this.body.getX());
        buf.writeFloat(this.body.getY());
        buf.writeFloat(this.body.getZ());

        buf.writeFloat(this.rightArm.getX());
        buf.writeFloat(this.rightArm.getY());
        buf.writeFloat(this.rightArm.getZ());

        buf.writeFloat(this.leftArm.getX());
        buf.writeFloat(this.leftArm.getY());
        buf.writeFloat(this.leftArm.getZ());

        buf.writeFloat(this.rightLeg.getX());
        buf.writeFloat(this.rightLeg.getY());
        buf.writeFloat(this.rightLeg.getZ());

        buf.writeFloat(this.leftLeg.getX());
        buf.writeFloat(this.leftLeg.getY());
        buf.writeFloat(this.leftLeg.getZ());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity = ctx.get().getSender().world.getEntityByID(this.entityId);

            if (entity instanceof ArmorStandEntity) {
                ArmorStandEntity armorStandEntity = (ArmorStandEntity) entity;
                armorStandEntity.setHeadRotation(this.head);
                armorStandEntity.setBodyRotation(this.body);
                armorStandEntity.setRightArmRotation(this.rightArm);
                armorStandEntity.setLeftArmRotation(this.leftArm);
                armorStandEntity.setRightLegRotation(this.rightLeg);
                armorStandEntity.setLeftLegRotation(this.leftLeg);
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
