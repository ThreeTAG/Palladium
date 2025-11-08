package net.threetag.palladium.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.entity.PalladiumPlayerExtension;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladiumcore.network.MessageC2S;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageType;
import org.jetbrains.annotations.NotNull;

public class RightClickAttackMessage extends MessageC2S {

    private final int targetId;

    public RightClickAttackMessage(int targetId) {
        this.targetId = targetId;
    }

    public RightClickAttackMessage(FriendlyByteBuf buf) {
        this.targetId = buf.readInt();
    }

    @Override
    public @NotNull MessageType getType() {
        return PalladiumNetwork.RIGHT_CLICK_ATTACK;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.targetId);
    }

    @Override
    public void handle(MessageContext context) {
        var player = context.getPlayer();

        if (AbilityUtil.isTypeEnabled(player, Abilities.DUAL_WIELDING.get()) && player instanceof PalladiumPlayerExtension ext) {
            var target = player.level().getEntity(this.targetId);

            if (target != null && !(target instanceof ItemEntity)
                    && !(target instanceof ExperienceOrb)
                    && !(target instanceof AbstractArrow)
                    && target != player) {
                ItemStack itemstack = player.getOffhandItem();
                if (itemstack.isItemEnabled(player.serverLevel().enabledFeatures())) {
                    ext.palladium$getDualWieldingHandler().attackWithOffHand(target);
                }
            } else {
                ext.palladium$getDualWieldingHandler().swing();
            }
        }

    }
}
