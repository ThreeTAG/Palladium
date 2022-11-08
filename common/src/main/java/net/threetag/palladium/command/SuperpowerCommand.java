package net.threetag.palladium.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.util.property.PalladiumProperties;
import net.threetag.palladiumcore.util.Platform;

import java.util.*;

public class SuperpowerCommand {

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_POWERS = (context, builder) -> {
        Collection<Power> powers = PowerManager.getInstance(Objects.requireNonNull(Platform.getCurrentServer()).overworld()).getPowers();
        return SharedSuggestionProvider.suggestResource(powers.stream().map(Power::getId), builder);
    };

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_OWN_POWERS = (context, builder) -> {
        List<ResourceLocation> superpowers = Lists.newArrayList();
        Collection<? extends Entity> entities;
        try {
            context.getArgument("entities", EntitySelector.class);
            entities = EntityArgument.getEntities(context, "entities");
        } catch (Exception e) {
            entities = Collections.singleton(context.getSource().getPlayerOrException());
        }
        for (Entity entity : entities) {
            for (ResourceLocation id : PalladiumProperties.SUPERPOWER_IDS.get(entity)) {
                if (!superpowers.contains(id)) {
                    superpowers.add(id);
                }
            }
        }
        return SharedSuggestionProvider.suggestResource(superpowers.stream(), builder);
    };

    public static final DynamicCommandExceptionType POWER_NOT_FOUND = new DynamicCommandExceptionType((object) -> {
        return Component.translatable("commands.superpower.error.powerNotFound", object);
    });

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("superpower").requires((player) -> {
                    return player.hasPermission(2);
                })

                .then(Commands.literal("set").then(Commands.argument("power", ResourceLocationArgument.id()).suggests(SUGGEST_POWERS).executes(c -> {
                    return setSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), getSuperpower(c, "power"));
                }).then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                    return setSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), getSuperpower(c, "power"));
                }))))

                .then(Commands.literal("add").then(Commands.argument("power", ResourceLocationArgument.id()).suggests(SUGGEST_POWERS).executes(c -> {
                    return addSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), getSuperpower(c, "power"));
                }).then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                    return addSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), getSuperpower(c, "power"));
                }))))

                .then(Commands.literal("remove").then(Commands.argument("power", ResourceLocationArgument.id()).suggests(SUGGEST_OWN_POWERS).executes(c -> {
                    return removeSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), ResourceLocationArgument.getId(c, "power"));
                }).then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                    return removeSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), ResourceLocationArgument.getId(c, "power"));
                }))).then(Commands.literal("*").executes(c -> {
                    return removeAllSuperpowers(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()));
                }).then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                    return removeAllSuperpowers(c.getSource(), EntityArgument.getEntities(c, "entities"));
                })))));
    }

    public static Power getSuperpower(CommandContext<CommandSourceStack> context, String key) throws CommandSyntaxException {
        ResourceLocation resourceLocation = context.getArgument(key, ResourceLocation.class);
        Power power = PowerManager.getInstance(context.getSource().getLevel()).getPower(resourceLocation);
        if (power == null) {
            throw POWER_NOT_FOUND.create(resourceLocation);
        } else {
            return power;
        }
    }

    public static int setSuperpower(CommandSourceStack commandSource, Collection<? extends Entity> entities, Power power) {
        Iterator<? extends Entity> iterator = entities.iterator();
        int i = 0;
        boolean no = false;

        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (entity instanceof LivingEntity) {
                PalladiumProperties.SUPERPOWER_IDS.set(entity, Collections.singletonList(power.getId()));
                i++;
            } else {
                commandSource.sendFailure(Component.translatable("commands.superpower.error.noLivingEntity"));
            }
        }

        if (i == 1) {
            commandSource.sendSuccess(Component.translatable("commands.superpower.success.entity.single", (entities.iterator().next()).getDisplayName(), power.getName()), true);
        } else {
            commandSource.sendSuccess(Component.translatable("commands.superpower.success.entity.multiple", i, power.getName()), true);
        }

        return i;
    }

    public static int addSuperpower(CommandSourceStack commandSource, Collection<? extends Entity> entities, Power superpower) {
        Iterator<? extends Entity> iterator = entities.iterator();
        int i = 0;
        boolean no = false;

        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (entity instanceof LivingEntity) {
                if (!PalladiumProperties.SUPERPOWER_IDS.get(entity).contains(superpower.getId())) {
                    List<ResourceLocation> powerIds = new ArrayList<>(PalladiumProperties.SUPERPOWER_IDS.get(entity));
                    powerIds.add(superpower.getId());
                    PalladiumProperties.SUPERPOWER_IDS.set(entity, powerIds);
                    i++;
                } else if (entities.size() == 1) {
                    no = true;
                    commandSource.sendFailure(Component.translatable("commands.superpower.error.alreadyHasSuperpower", entity.getDisplayName()));
                }
            } else {
                commandSource.sendFailure(Component.translatable("commands.superpower.error.noLivingEntity"));
            }
        }

        if (!no) {
            if (i == 1) {
                commandSource.sendSuccess(Component.translatable("commands.superpower.success.entity.single", (entities.iterator().next()).getDisplayName(), superpower.getName()), true);
            } else {
                commandSource.sendSuccess(Component.translatable("commands.superpower.success.entity.multiple", i, superpower.getName()), true);
            }
        }

        return i;
    }

    public static int removeSuperpower(CommandSourceStack commandSource, Collection<? extends Entity> entities, ResourceLocation id) {
        Iterator<? extends Entity> iterator = entities.iterator();
        int i = 0;
        boolean no = false;

        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (entity instanceof LivingEntity) {
                if (PalladiumProperties.SUPERPOWER_IDS.get(entity).contains(id)) {
                    List<ResourceLocation> powerIds = new ArrayList<>(PalladiumProperties.SUPERPOWER_IDS.get(entity));
                    powerIds.remove(id);
                    PalladiumProperties.SUPERPOWER_IDS.set(entity, powerIds);
                    i++;
                } else if (entities.size() == 1) {
                    no = true;
                    commandSource.sendFailure(Component.translatable("commands.superpower.error.doesntHaveSuperpower", entity.getDisplayName()));
                }
            } else {
                commandSource.sendFailure(Component.translatable("commands.superpower.error.noLivingEntity"));
            }
        }

        if (!no) {
            if (i == 1) {
                commandSource.sendSuccess(Component.translatable("commands.superpower.remove.success.entity.single", (entities.iterator().next()).getDisplayName()), true);
            } else {
                commandSource.sendSuccess(Component.translatable("commands.superpower.remove.success.entity.multiple", i), true);
            }
        }

        return i;
    }

    public static int removeAllSuperpowers(CommandSourceStack commandSource, Collection<? extends Entity> entities) {
        Iterator<? extends Entity> iterator = entities.iterator();
        int i = 0;

        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (entity instanceof LivingEntity) {
                PalladiumProperties.SUPERPOWER_IDS.set(entity, new ArrayList<>());
            } else {
                commandSource.sendFailure(Component.translatable("commands.superpower.error.noLivingEntity"));
            }
        }

        if (i == 1) {
            commandSource.sendSuccess(Component.translatable("commands.superpower.remove.success.entity.single", (entities.iterator().next()).getDisplayName()), true);
        } else {
            commandSource.sendSuccess(Component.translatable("commands.superpower.remove.success.entity.multiple", i), true);
        }

        return i;
    }

}