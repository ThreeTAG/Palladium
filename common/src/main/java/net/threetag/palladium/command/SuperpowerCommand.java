package net.threetag.palladium.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.architectury.utils.GameInstance;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.provider.SuperpowerProvider;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

public class SuperpowerCommand {

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_POWERS = (context, builder) -> {
        Collection<Power> powers = PowerManager.getInstance(Objects.requireNonNull(GameInstance.getServer()).overworld()).getPowers();
        return SharedSuggestionProvider.suggestResource(powers.stream().map(Power::getId), builder);
    };

    public static final DynamicCommandExceptionType POWER_NOT_FOUND = new DynamicCommandExceptionType((object) -> {
        return new TranslatableComponent("commands.superpower.error.powerNotFound", object);
    });

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("superpower").requires((player) -> {
                    return player.hasPermission(2);
                })

                .then(Commands.literal("set").then(Commands.argument("power", ResourceLocationArgument.id()).suggests(SUGGEST_POWERS).executes(c -> {
                    return setSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), getPower(c, "power"), -1);
                }).then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                    return setSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), getPower(c, "power"), -1);
                }))))

                .then(Commands.literal("remove").executes(c -> {
                    return removeSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()));
                }).then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                    return removeSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"));
                }))));
    }

    public static Power getPower(CommandContext<CommandSourceStack> context, String key) throws CommandSyntaxException {
        ResourceLocation resourceLocation = context.getArgument(key, ResourceLocation.class);
        Power power = PowerManager.getInstance(context.getSource().getLevel()).getPower(resourceLocation);
        if (power == null) {
            throw POWER_NOT_FOUND.create(resourceLocation);
        } else {
            return power;
        }
    }

    public static int setSuperpower(CommandSourceStack commandSource, Collection<? extends Entity> entities, Power superpower, int lifetime) {
        Iterator<? extends Entity> iterator = entities.iterator();
        int i = 0;
        boolean no = false;

        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (entity instanceof LivingEntity) {
                SuperpowerProvider.SUPERPOWER_ID.set(entity, superpower.getId());
                i++;
            } else {
                commandSource.sendFailure(new TranslatableComponent("commands.superpower.error.noLivingEntity"));
            }
        }

        if (i == 1) {
            commandSource.sendSuccess(new TranslatableComponent("commands.superpower.success.entity.single", (entities.iterator().next()).getDisplayName(), superpower.getName()), true);
        } else {
            commandSource.sendSuccess(new TranslatableComponent("commands.superpower.success.entity.multiple", i, superpower.getName()), true);
        }

        return i;
    }

    public static int removeSuperpower(CommandSourceStack commandSource, Collection<? extends Entity> entities) {
        Iterator<? extends Entity> iterator = entities.iterator();
        int i = 0;
        boolean no = false;

        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (entity instanceof LivingEntity) {
                SuperpowerProvider.SUPERPOWER_ID.set(entity, null);
                i++;
            } else {
                commandSource.sendFailure(new TranslatableComponent("commands.superpower.error.noLivingEntity"));
            }
        }

        if (!no) {
            if (i == 1) {
                commandSource.sendSuccess(new TranslatableComponent("commands.superpower.remove.success.entity.single", (entities.iterator().next()).getDisplayName()), true);
            } else {
                commandSource.sendSuccess(new TranslatableComponent("commands.superpower.remove.success.entity.multiple", i), true);
            }
        }

        return i;
    }

}