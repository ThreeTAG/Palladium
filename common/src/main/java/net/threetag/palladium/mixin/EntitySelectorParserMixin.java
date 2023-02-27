package net.threetag.palladium.mixin;

import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.command.EntitySelectorParserExtension;
import net.threetag.palladium.power.ability.AbilityUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(EntitySelectorParser.class)
public class EntitySelectorParserMixin implements EntitySelectorParserExtension {

    @Shadow
    private Predicate<Entity> predicate;
    ResourceLocation palladium$powerId = null;

    @Override
    public ResourceLocation palladium$getPower() {
        return this.palladium$powerId;
    }

    @Override
    public void palladium$setPower(ResourceLocation powerId) {
        this.palladium$powerId = powerId;
    }

    @Inject(method = "finalizePredicates", at = @At("HEAD"))
    private void finalizePredicates(CallbackInfo info) {
        if (this.palladium$powerId != null) {
            this.predicate = this.predicate.and(e -> e instanceof LivingEntity livingEntity && AbilityUtil.hasPower(livingEntity, this.palladium$powerId));
        }
    }
}
