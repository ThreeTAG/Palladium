package net.threetag.threecore.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.threetag.threecore.scripts.ScriptEventManager;
import net.threetag.threecore.scripts.events.MultiJumpScriptEvent;
import net.threetag.threecore.util.icon.TexturedIcon;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.IntegerThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class MultiJumpAbility extends Ability {

    public static final ThreeData<Integer> JUMPS = new IntegerThreeData("jumps").setSyncType(EnumSync.NONE).enableSetting("Determines how often the player can jump in the air");

    public int jumps;

    public MultiJumpAbility() {
        super(AbilityType.MULTI_JUMP);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.register(ICON, new TexturedIcon(TexturedIcon.ICONS_TEXTURE, 144, 16, 16, 16));
        this.register(JUMPS, 2);
    }

    @Override
    public void action(LivingEntity entity) {
        if (entity.onGround && this.jumps > 0) {
            this.jumps = 0;
        }
    }

    public void jump(PlayerEntity player) {
        if (!player.onGround && this.jumps < this.get(JUMPS) - 1) {
            this.jumps++;
            MultiJumpScriptEvent event = new MultiJumpScriptEvent(player, this, this.jumps);
            ScriptEventManager.fireEvent(event);

            if (!event.isCancelled()) {
                player.jump();
                player.fallDistance = 0F;
                ((ServerPlayerEntity) player).connection.sendPacket(new SEntityVelocityPacket(player));
            }
        }
    }
}
