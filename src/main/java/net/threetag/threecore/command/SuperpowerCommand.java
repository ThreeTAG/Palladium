package net.threetag.threecore.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.threetag.threecore.ability.superpower.Superpower;
import net.threetag.threecore.ability.superpower.SuperpowerManager;

import java.util.Collection;
import java.util.Iterator;

public class SuperpowerCommand {

    private static final SuggestionProvider<CommandSource> SUGGEST_SUPERPOWERS = (context, builder) -> {
        Collection<Superpower> superpowers = SuperpowerManager.getInstance().getSuperpowers();
        return ISuggestionProvider.func_212476_a(superpowers.stream().map(Superpower::getId), builder);
    };

    public static final DynamicCommandExceptionType SUPERPOWER_NOT_FOUND = new DynamicCommandExceptionType((object) -> {
        return new TranslationTextComponent("commands.superpower.error.superpowerNotFound", object);
    });

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("superpower").requires((player) -> {
            return player.hasPermissionLevel(2);
        }).then(Commands.argument("entities", EntityArgument.entities()).then(Commands.argument("superpower", ResourceLocationArgument.resourceLocation()).suggests(SUGGEST_SUPERPOWERS).executes((c) -> {
            return setSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), getSuperpower(c, "superpower"));
        })).then(Commands.literal("remove").executes(c -> {
            return removeSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"));
        }))));
    }

    public static Superpower getSuperpower(CommandContext<CommandSource> context, String key) throws CommandSyntaxException {
        ResourceLocation resourceLocation = context.getArgument(key, ResourceLocation.class);
        Superpower superpower = SuperpowerManager.getInstance().getSuperpower(resourceLocation);
        if (superpower == null) {
            throw SUPERPOWER_NOT_FOUND.create(resourceLocation);
        } else {
            return superpower;
        }
    }

    public static int setSuperpower(CommandSource commandSource, Collection<? extends Entity> entities, Superpower superpower) {
        Iterator<? extends Entity> iterator = entities.iterator();
        int i = 0;

        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (entity instanceof LivingEntity) {
                SuperpowerManager.setSuperpower((LivingEntity) entity, superpower);
                i++;
            } else {
                commandSource.sendErrorMessage(new TranslationTextComponent("commands.superpower.error.noLivingEntity"));
            }
        }

        if (i == 1) {
            commandSource.sendFeedback(new TranslationTextComponent("commands.superpower.success.entity.single", (entities.iterator().next()).getDisplayName(), superpower.getName()), true);
        } else {
            commandSource.sendFeedback(new TranslationTextComponent("commands.superpower.success.entity.multiple", i, superpower.getName()), true);
        }

        return i;
    }

    public static int removeSuperpower(CommandSource commandSource, Collection<? extends Entity> entities) {
        Iterator<? extends Entity> iterator = entities.iterator();
        int i = 0;

        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (entity instanceof LivingEntity) {
                SuperpowerManager.removeSuperpower((LivingEntity) entity);
                i++;
            } else {
                commandSource.sendErrorMessage(new TranslationTextComponent("commands.superpower.error.noLivingEntity"));
            }
        }

        if (i == 1) {
            commandSource.sendFeedback(new TranslationTextComponent("commands.superpower.remove.success.entity.single", (entities.iterator().next()).getDisplayName()), true);
        } else {
            commandSource.sendFeedback(new TranslationTextComponent("commands.superpower.remove.success.entity.multiple", i), true);
        }

        return i;
    }

}