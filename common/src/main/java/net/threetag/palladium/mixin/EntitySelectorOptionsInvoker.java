package net.threetag.palladium.mixin;

import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.commands.arguments.selector.options.EntitySelectorOptions;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Predicate;

@Mixin(EntitySelectorOptions.class)
public interface EntitySelectorOptionsInvoker {

    @Invoker
    static void callRegister(String id, EntitySelectorOptions.Modifier modifier, Predicate<EntitySelectorParser> predicate, Component tooltip) {
        throw new NoSuchMethodError();
    }

}
