package com.threetag.threecore.sizechanging.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.threetag.threecore.sizechanging.SizeChangeType;
import com.threetag.threecore.sizechanging.capability.CapabilitySizeChanging;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class SizeChangeCommand {

    private static final SuggestionProvider<CommandSource> SUGGEST_SIZE_CHANGES_TYPES = (context, builder) -> {
        Collection<SizeChangeType> sizeChangeTypes = SizeChangeType.REGISTRY.getValues();
        return ISuggestionProvider.func_212476_a(sizeChangeTypes.stream().map(SizeChangeType::getRegistryName), builder);
    };

    public static final DynamicCommandExceptionType SIZE_CHANGE_TYPE_NOT_FOUND = new DynamicCommandExceptionType((object) -> new TranslationTextComponent("commands.sizechange.error.sizeChangeTypeNotFound", object));

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("sizechange").requires((sender) -> sender.hasPermissionLevel(2))
                .then(Commands.argument("entities", EntityArgument.entities()).then(Commands.argument("scale", FloatArgumentType.floatArg(CapabilitySizeChanging.MIN_SIZE, CapabilitySizeChanging.MAX_SIZE)).executes(c -> {
                    return setScale(c.getSource(), EntityArgument.getEntities(c, "entities"), FloatArgumentType.getFloat(c, "scale"), null);
                }).then(Commands.argument("size_change_type", ResourceLocationArgument.resourceLocation()).suggests(SUGGEST_SIZE_CHANGES_TYPES).executes(c -> {
                    return setScale(c.getSource(), EntityArgument.getEntities(c, "entities"), FloatArgumentType.getFloat(c, "scale"), getSizeChangeType(c, "size_change_type"));
                })))));
    }

    private static int setScale(CommandSource commandSource, Collection<? extends Entity> entities, float scale, @Nullable SizeChangeType sizeChangeType) {
        AtomicInteger result = new AtomicInteger(0);
        entities.forEach(e -> {
            e.getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
                if (sizeChanging.startSizeChange(e, sizeChangeType, scale))
                    result.incrementAndGet();
            });
        });

        if (result.get() == 1) {
            commandSource.sendFeedback(new TranslationTextComponent("commands.sizechange.success.entity.single", (entities.iterator().next()).getDisplayName(), scale, sizeChangeType == null ? SizeChangeType.DEFAULT_TYPE.getRegistryName() : sizeChangeType.getRegistryName()), true);
        } else {
            commandSource.sendFeedback(new TranslationTextComponent("commands.sizechange.success.entity.multiple", result.get(), scale, sizeChangeType == null ? SizeChangeType.DEFAULT_TYPE.getRegistryName() : sizeChangeType.getRegistryName()), true);
        }

        return result.get();
    }

    public static SizeChangeType getSizeChangeType(CommandContext<CommandSource> context, String key) throws CommandSyntaxException {
        ResourceLocation resourceLocation = (ResourceLocation) context.getArgument(key, ResourceLocation.class);
        SizeChangeType sizeChangeType = SizeChangeType.REGISTRY.getValue(resourceLocation);
        if (sizeChangeType == null) {
            throw SIZE_CHANGE_TYPE_NOT_FOUND.create(resourceLocation);
        } else {
            return sizeChangeType;
        }
    }

}
