package net.threetag.palladium.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.threetag.palladium.client.beam.BeamManager;
import net.threetag.palladium.client.gui.screen.abilitybar.AbilityBar;
import net.threetag.palladium.client.gui.ui.screen.UiScreenManager;
import net.threetag.palladium.client.gui.widget.PowerTreeWidget;
import net.threetag.palladium.client.particleemitter.ParticleEmitterManager;
import net.threetag.palladium.component.PalladiumDataComponents;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.customization.EntityCustomizationHandler;
import net.threetag.palladium.entity.EffectEntity;
import net.threetag.palladium.entity.effect.EntityEffect;
import net.threetag.palladium.network.*;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerInstance;
import net.threetag.palladium.power.PowerUtil;
import net.threetag.palladium.power.PowerValidator;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.unlocking.BuyableUnlockingHandler;
import net.threetag.palladium.sound.AbilitySound;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class PalladiumClientProxy extends PalladiumProxy {

    @Override
    public void sendPacketToServer(CustomPacketPayload payload, CustomPacketPayload... payloads) {
        ClientPacketDistributor.sendToServer(payload, payloads);
    }

    @Override
    public void spawnEffectEntity(Entity anchor, EntityEffect entityEffect) {
        EffectEntity effectEntity = new EffectEntity(anchor.level(), anchor, entityEffect);
        Objects.requireNonNull(Minecraft.getInstance().level).addEntity(effectEntity);
    }

    @Override
    public void openScreen(Identifier screenId) {
        var screen = UiScreenManager.INSTANCE.get(screenId);

        if (screen != null) {
            screen.open();
        }
    }

    @Override
    public Collection<Identifier> getAvailableScreenIds() {
        return UiScreenManager.INSTANCE.getIds();
    }

    @Override
    // TODO test
    public boolean hasMovementInput(Entity entity) {
        if (entity.getId() == Objects.requireNonNull(Minecraft.getInstance().player).getId()) {
            var options = Minecraft.getInstance().options;
            return options.keyUp.isDown() || options.keyDown.isDown() || options.keyRight.isDown() || options.keyLeft.isDown() || options.keyJump.isDown();
        }

        return super.hasMovementInput(entity);
    }

    @Override
    public Input getMovementInput(Entity entity) {
        if (entity.getId() == Objects.requireNonNull(Minecraft.getInstance().player).getId()) {
            var options = Minecraft.getInstance().options;
            return new Input(
                    options.keyUp.isDown(),
                    options.keyDown.isDown(),
                    options.keyLeft.isDown(),
                    options.keyRight.isDown(),
                    options.keyJump.isDown(),
                    options.keyShift.isDown(),
                    options.keySprint.isDown()
            );
        }

        return super.getMovementInput(entity);
    }

    @Override
    public boolean playerHasSlimModel(Avatar player) {
        if (player instanceof ClientAvatarEntity avatar)
            return avatar.getSkin().model() == PlayerModelType.SLIM;
        return false;
    }

    @Override
    public void spawnEnergyBeamParticles(Level level, Vec3 pos, Identifier beamId) {
        var beam = BeamManager.INSTANCE.get(beamId);

        if (beam != null) {
            beam.spawnParticles(level, pos);
        }
    }

    @Override
    public void spawnParticleEmitter(LivingEntity entity, List<Identifier> particleEmitterIds, Holder<ParticleType<?>> particleTypeHolder, CompoundTag options) {
        if (entity instanceof AbstractClientPlayer player) {
            ParticleType<?> type = particleTypeHolder.value();
            // TODO optimise: only parse once
            ParticleOptions particleOptions = type.codec().codec().parse(entity.registryAccess().createSerializationContext(NbtOps.INSTANCE), options).getOrThrow();
            for (Identifier id : particleEmitterIds) {
                var emitter = ParticleEmitterManager.INSTANCE.get(id);

                if (emitter != null) {
                    emitter.spawnParticles(entity.level(), player, particleOptions, Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks());
                }
            }
        }
    }

    @Override
    public void applyShader(LivingEntity entity, Identifier shader) {
        var mc = Minecraft.getInstance();

        if (entity == mc.player) {
            mc.gameRenderer.setPostEffect(shader);
        }
    }

    @Override
    public void removeShader(LivingEntity entity, Identifier shader) {
        var mc = Minecraft.getInstance();
        var current = mc.gameRenderer.currentPostEffect();

        if (entity == mc.player && current != null && current.equals(shader)) {
            mc.gameRenderer.clearPostEffect();
        }
    }

    @Override
    public void playAbilitySound(AbilityInstance<?> abilityInstance, LivingEntity entity, Identifier sound, float volume, float pitch, boolean playSelf) {
        if (!playSelf || Minecraft.getInstance().player == entity) {
            Minecraft.getInstance().getSoundManager().play(new AbilitySound(abilityInstance.getReference(), entity, sound, entity.getSoundSource(), volume, pitch));
        }
    }

    @Override
    public void packetHandleOpenAbilityBuyScreen(OpenAbilityBuyScreenPacket packet, IPayloadContext context) {
        packet.reference().optional(context.player(), null).ifPresent(ability -> {
            var screen = Minecraft.getInstance().screen;
            if (screen != null && ability.getAbility().getStateManager().getUnlockingHandler() instanceof BuyableUnlockingHandler buy) {
                for (GuiEventListener child : screen.children()) {
                    if (child instanceof PowerTreeWidget powerTree) {
                        for (PowerTreeWidget.AbilityElement abilityElement : powerTree.abilities) {
                            if (abilityElement.getReference().equals(ability.getReference())) {
                                abilityElement.openModal(screen, buy.getDisplay(), packet.available());
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void packetHandleSyncAbilityComponent(SyncAbilityComponentPacket packet, IPayloadContext context) {
        Level level = Minecraft.getInstance().level;
        if (level != null && level.getEntity(packet.entityId()) instanceof LivingEntity livingEntity) {
            packet.reference().optional(livingEntity, null).ifPresent(ability -> {
                boolean prevEnabled = ability.isEnabled();
                ability.applyPatch(packet.patch());
                var optional = packet.patch().get(PalladiumDataComponents.Abilities.ENABLED.get());

                if (optional != null && optional.isPresent()) {
                    optional.ifPresent(enabled -> {
                        if (enabled != prevEnabled) {
                            if (enabled) {
                                ability.getAbility().firstTick(livingEntity, ability);
                            } else if (ability.getLifetime() > 0) {
                                ability.getAbility().lastTick(livingEntity, ability);
                            }
                        }
                    });
                }

                if (livingEntity == Minecraft.getInstance().player) {
                    AbilityBar.INSTANCE.populate();
                }
            });
        }
    }

    @Override
    public void packetHandleSyncEntityCustomization(SyncEntityCustomizationPacket packet, IPayloadContext context) {
        Level level = Minecraft.getInstance().level;
        if (level != null && level.getEntity(packet.entityId()) instanceof LivingEntity livingEntity) {
            var handler = EntityCustomizationHandler.get(livingEntity);
            handler.select(packet.customization());
        }
    }

    @Override
    public void packetHandleSyncEntityCustomizations(SyncEntityCustomizationsPacket packet, IPayloadContext context) {
        Level level = Minecraft.getInstance().level;
        if (level != null && level.getEntity(packet.entityId()) instanceof LivingEntity livingEntity) {
            var handler = EntityCustomizationHandler.get(livingEntity);
            for (Holder<Customization> customization : packet.selected()) {
                handler.select(customization);
            }
        }
    }

    @Override
    public void packetHandleSyncEntityUnselectCustomization(SyncEntityUnselectCustomizationPacket packet, IPayloadContext context) {
        Level level = Minecraft.getInstance().level;
        if (level != null && level.getEntity(packet.entityId()) instanceof LivingEntity livingEntity) {
            var handler = EntityCustomizationHandler.get(livingEntity);
            handler.unselect(packet.category());
        }
    }

    @Override
    public void packetHandleSyncEntityPowers(SyncEntityPowersPacket packet, IPayloadContext context) {
        Level level = Minecraft.getInstance().level;

        if (level != null && level.getEntity(packet.entityId()) instanceof LivingEntity livingEntity) {
            var handler = PowerUtil.getPowerHandler(livingEntity);

            for (Holder<Power> power : packet.remove()) {
                handler.removePowerInstance(power);
            }

            for (SyncEntityPowersPacket.NewPowerChange add : packet.add()) {
                var powerInstance = new PowerInstance(livingEntity, add.power, PowerValidator.ALWAYS_ACTIVE, add.priority, new CompoundTag());
                handler.addPowerInstance(powerInstance);

                for (Pair<String, DataComponentPatch> abilityComponent : add.abilityComponents) {
                    var ability = powerInstance.getAbilities().get(abilityComponent.getLeft());
                    if (ability != null) {
                        ability.applyPatch(abilityComponent.getRight());
                    }
                }

                for (Triple<String, Integer, Integer> pair : add.energyBars) {
                    var bar = powerInstance.getEnergyBars().get(pair.getLeft());

                    if (bar != null) {
                        bar.set(pair.getMiddle());
                        bar.setMax(pair.getRight());
                    }
                }
            }

            if (livingEntity == Minecraft.getInstance().player) {
                AbilityBar.INSTANCE.populate();
            }
        }
    }
}
