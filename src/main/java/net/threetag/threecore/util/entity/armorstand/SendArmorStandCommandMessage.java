package net.threetag.threecore.util.entity.armorstand;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.threetag.threecore.ThreeCore;

import java.util.function.Supplier;

public class SendArmorStandCommandMessage {

    private String argument;

    public SendArmorStandCommandMessage(String argument) {
        this.argument = argument;
    }

    public SendArmorStandCommandMessage(PacketBuffer packetBuffer) {
        this.argument = packetBuffer.readString();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeString(this.argument);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(this.argument.equalsIgnoreCase("reload")) {
                int i = ArmorStandPoseManager.load();
                Minecraft.getInstance().player.sendMessage(new TranslationTextComponent("commands.armorstandpose.reloaded", i));
            } else {
                ArmorStandPoseManager.ArmorStandPose pose = ArmorStandPoseManager.POSES.get(argument);
                if (pose == null)
                    Minecraft.getInstance().player.sendMessage(new TranslationTextComponent("commands.armorstandpose.pose_not_found", argument).applyTextStyle(TextFormatting.RED));
                else {
                    RayTraceResult res = Minecraft.getInstance().objectMouseOver;
                    if (res instanceof EntityRayTraceResult && ((EntityRayTraceResult) res).getEntity() instanceof ArmorStandEntity) {
                        ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.SERVER.noArg(), new SetArmorStandPoseMessage(((EntityRayTraceResult) res).getEntity().getEntityId(),
                                pose.head, pose.body, pose.rightArm, pose.leftArm, pose.rightLeg, pose.leftLeg));
                    } else {
                        Minecraft.getInstance().player.sendMessage(new TranslationTextComponent("commands.armorstandpose.no_armor_stand").applyTextStyle(TextFormatting.RED));
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
