package net.threetag.palladium.proxy;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.threetag.palladium.entity.effect.EntityEffect;
import net.threetag.palladium.network.*;
import net.threetag.palladium.power.ability.AbilityInstance;

import java.util.List;

public class PalladiumProxy {

    public void sendPacketToServer(CustomPacketPayload payload, CustomPacketPayload... payloads) {

    }

    public void spawnEffectEntity(Entity anchor, EntityEffect entityEffect) {

    }

    public void openScreen(Identifier screenId) {

    }

    public boolean hasMovementInput(Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            var input = serverPlayer.getLastClientInput();
            return input.forward() || input.backward() || input.left() || input.right() || input.jump();
        }

        return false;
    }

    public Input getMovementInput(Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            return serverPlayer.getLastClientInput();
        }

        return null;
    }

    public boolean playerHasSlimModel(Avatar player) {
        return false;
    }

    public void spawnEnergyBeamParticles(Level level, Vec3 pos, Identifier beamId) {

    }

    public void spawnParticleEmitter(LivingEntity entity, List<Identifier> particleEmitterIds, Holder<ParticleType<?>> particleTypeHolder, CompoundTag options) {

    }

    public void applyShader(LivingEntity entity, Identifier shader) {

    }

    public void removeShader(LivingEntity entity, Identifier shader) {

    }

    public void playAbilitySound(AbilityInstance<?> abilityInstance, LivingEntity entity, Identifier sound, float volume, float pitch, boolean playSelf) {

    }

    public void packetHandleOpenAbilityBuyScreen(OpenAbilityBuyScreenPacket packet, IPayloadContext context) {

    }

    public void packetHandleSyncAbilityComponent(SyncAbilityComponentPacket packet, IPayloadContext context) {

    }

    public void packetHandleSyncEntityCustomization(SyncEntityCustomizationPacket packet, IPayloadContext context) {

    }

    public void packetHandleSyncEntityCustomizations(SyncEntityCustomizationsPacket packet, IPayloadContext context) {

    }

    public void packetHandleSyncEntityUnselectCustomization(SyncEntityUnselectCustomizationPacket packet, IPayloadContext context) {

    }

    public void packetHandleSyncEntityPowers(SyncEntityPowersPacket packet, IPayloadContext context) {

    }

}
