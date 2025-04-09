package net.threetag.palladium.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.EntityGlowAbility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class ClientEntityMixin {

    @Inject(method = "getTeamColor", at = @At("HEAD"), cancellable = true)
    private void getTeamColor(CallbackInfoReturnable<Integer> cir) {
        Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
        Entity renderedEntity = (Entity) (Object) this;
        int i = 0;
        float red = 0F;
        float green = 0F;
        float blue = 0F;

        if (renderedEntity instanceof LivingEntity living) {
            for (AbilityInstance ability : AbilityUtil.getEnabledInstances(living, Abilities.ENTITY_GLOW.get())) {
                if (ability.getProperty(EntityGlowAbility.MODE) == EntityGlowAbility.Mode.SELF) {
                    var color = ability.getProperty(EntityGlowAbility.COLOR);

                    if (color != null) {
                        red += color.getRed() / 255F;
                        green += color.getGreen() / 255F;
                        blue += color.getBlue() / 255F;
                        i++;
                    }
                }
            }
        }

        if (cameraEntity instanceof LivingEntity living) {
            for (AbilityInstance ability : AbilityUtil.getEnabledInstances(living, Abilities.ENTITY_GLOW.get())) {
                if (ability.getProperty(EntityGlowAbility.MODE) == EntityGlowAbility.Mode.OTHERS) {
                    var color = ability.getProperty(EntityGlowAbility.COLOR);

                    if (color != null) {
                        red += color.getRed() / 255F;
                        green += color.getGreen() / 255F;
                        blue += color.getBlue() / 255F;
                        i++;
                    }
                }
            }
        }

        if (i > 0) {
            cir.setReturnValue(Mth.color(red / i, green / i, blue / i));
        }
    }

}
