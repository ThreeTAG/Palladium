package net.threetag.palladium.mixin;

import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.command.EntitySelectorParserExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(EntitySelectorParser.class)
public abstract class EntitySelectorParserMixin implements EntitySelectorParserExtension {

    @Shadow
    private Predicate<Entity> predicate;

    @Shadow
    public abstract boolean shouldInvertValue();

    @Unique
    ResourceLocation palladium$powerId = null;

    @Override
    public ResourceLocation palladium$getPower() {
        return this.palladium$powerId;
    }

    @Override
    public void palladium$setPower(ResourceLocation powerId) {
        this.palladium$powerId = powerId;
    }
}
