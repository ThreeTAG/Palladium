package net.threetag.palladium.power.ability;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;
import net.threetag.palladium.util.property.SyncType;
import net.threetag.palladiumcore.util.Platform;

public class ShaderEffectAbility extends Ability {

    public static final PalladiumProperty<ResourceLocation> SHADER = new ResourceLocationProperty("shader").configurable("ID of the shader that shall be applied").sync(SyncType.SELF);

    public ShaderEffectAbility() {
        this.withProperty(SHADER, new ResourceLocation("shaders/post/creeper.json"));
    }

    @Override
    public boolean isEffect() {
        return true;
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (enabled && Platform.isClient()) {
            this.applyShader(entity, entry.getProperty(SHADER));
        }
    }

    @Override
    public void lastTick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (enabled && Platform.isClient()) {
            this.removeShader(entity, entry.getProperty(SHADER));
        }
    }

    @Environment(EnvType.CLIENT)
    public void applyShader(LivingEntity entity, ResourceLocation shader) {
        var mc = Minecraft.getInstance();

        if (entity == mc.player) {
            mc.gameRenderer.loadEffect(shader);
        }
    }

    @Environment(EnvType.CLIENT)
    public void removeShader(LivingEntity entity, ResourceLocation shader) {
        var mc = Minecraft.getInstance();

        if (entity == mc.player && mc.gameRenderer.currentEffect() != null && mc.gameRenderer.currentEffect().getName().equals(shader.toString())) {
            mc.gameRenderer.shutdownEffect();
        }
    }

    @Environment(EnvType.CLIENT)
    public static ResourceLocation get(Player player) {
        for (AbilityEntry entry : Ability.getEnabledEntries(player, Abilities.SHADER_EFFECT.get())) {
            return entry.getProperty(SHADER);
        }
        return null;
    }

}
