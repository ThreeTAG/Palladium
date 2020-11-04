package net.threetag.threecore.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.EntitySelector;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.threetag.threecore.ability.superpower.Superpower;
import net.threetag.threecore.ability.superpower.SuperpowerManager;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SuperpowerCommand {

    private static final SuggestionProvider<CommandSource> SUGGEST_SUPERPOWERS = (context, builder) -> {
        Collection<Superpower> superpowers = SuperpowerManager.getInstance().getSuperpowers();
        return ISuggestionProvider.func_212476_a(superpowers.stream().map(Superpower::getId), builder);
    };

    private static final SuggestionProvider<CommandSource> SUGGEST_OWN_SUPERPOWERS = (context, builder) -> {
        List<ResourceLocation> superpowers = Lists.newArrayList();
        Collection<? extends Entity> entities = null;
        try {
            context.getArgument("entities", EntitySelector.class);
            entities = EntityArgument.getEntities(context, "entities");
        } catch (Exception e) {
            entities = Collections.singleton(context.getSource().asPlayer());
        }
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity) {
                for (ResourceLocation id : SuperpowerManager.getSuperpowers((LivingEntity) entity)) {
                    if (!superpowers.contains(id)) {
                        superpowers.add(id);
                    }
                }
            }
        }
        return ISuggestionProvider.func_212476_a(superpowers.stream(), builder);
    };

    public static final DynamicCommandExceptionType SUPERPOWER_NOT_FOUND = new DynamicCommandExceptionType((object) -> {
        return new TranslationTextComponent("commands.superpower.error.superpowerNotFound", object);
    });

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("superpower").requires((player) -> {
            return player.hasPermissionLevel(2);
        })

                .then(Commands.literal("set").then(Commands.argument("superpower", ResourceLocationArgument.resourceLocation()).suggests(SUGGEST_SUPERPOWERS).executes(c -> {
                    return setSuperpower(c.getSource(), Collections.singleton(c.getSource().asPlayer()), getSuperpower(c, "superpower"), -1);
                }).then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                    return setSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), getSuperpower(c, "superpower"), -1);
                }).then(Commands.argument("lifetime", IntegerArgumentType.integer(0)).executes(c -> {
                    return setSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), getSuperpower(c, "superpower"), IntegerArgumentType.getInteger(c, "lifetime"));
                })))))

                .then(Commands.literal("add").then(Commands.argument("superpower", ResourceLocationArgument.resourceLocation()).suggests(SUGGEST_SUPERPOWERS).executes(c -> {
                    return addSuperpower(c.getSource(), Collections.singleton(c.getSource().asPlayer()), getSuperpower(c, "superpower"), -1);
                }).then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                    return addSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), getSuperpower(c, "superpower"), -1);
                }).then(Commands.argument("lifetime", IntegerArgumentType.integer(0)).executes(c -> {
                    return addSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), getSuperpower(c, "superpower"), IntegerArgumentType.getInteger(c, "lifetime"));
                })))))

                .then(Commands.literal("remove").then(Commands.argument("superpower", ResourceLocationArgument.resourceLocation()).suggests(SUGGEST_OWN_SUPERPOWERS).executes(c -> {
                    return removeSuperpower(c.getSource(), Collections.singleton(c.getSource().asPlayer()), ResourceLocationArgument.getResourceLocation(c, "superpower"));
                }).then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                    return removeSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), ResourceLocationArgument.getResourceLocation(c, "superpower"));
                }))).then(Commands.literal("*").executes(c -> {
                    return removeAllSuperpowers(c.getSource(), Collections.singleton(c.getSource().asPlayer()));
                }).then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                    return removeAllSuperpowers(c.getSource(), EntityArgument.getEntities(c, "entities"));
                })))));
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

    public static int setSuperpower(CommandSource commandSource, Collection<? extends Entity> entities, Superpower superpower, int lifetime) {
        Iterator<? extends Entity> iterator = entities.iterator();
        int i = 0;
        boolean no = false;

        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (entity instanceof LivingEntity) {
                SuperpowerManager.setSuperpower((LivingEntity) entity, superpower, lifetime);
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

    public static int addSuperpower(CommandSource commandSource, Collection<? extends Entity> entities, Superpower superpower, int lifetime) {
        Iterator<? extends Entity> iterator = entities.iterator();
        int i = 0;
        boolean no = false;

        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (entity instanceof LivingEntity) {
                if (SuperpowerManager.addSuperpower((LivingEntity) entity, superpower, lifetime)) {
                    i++;
                } else if (entities.size() == 1) {
                    no = true;
                    commandSource.sendErrorMessage(new TranslationTextComponent("commands.superpower.error.alreadyHasSuperpower", entity.getDisplayName()));
                }
            } else {
                commandSource.sendErrorMessage(new TranslationTextComponent("commands.superpower.error.noLivingEntity"));
            }
        }

        if (!no) {
            if (i == 1) {
                commandSource.sendFeedback(new TranslationTextComponent("commands.superpower.add.success.entity.single", (entities.iterator().next()).getDisplayName(), superpower.getName()), true);
            } else {
                commandSource.sendFeedback(new TranslationTextComponent("commands.superpower.add.success.entity.multiple", i, superpower.getName()), true);
            }
        }

        return i;
    }

    public static int removeSuperpower(CommandSource commandSource, Collection<? extends Entity> entities, ResourceLocation id) {
        Iterator<? extends Entity> iterator = entities.iterator();
        int i = 0;
        boolean no = false;

        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (entity instanceof LivingEntity) {
                if (SuperpowerManager.removeSuperpower((LivingEntity) entity, id)) {
                    i++;
                } else if (entities.size() == 1) {
                    no = true;
                    commandSource.sendErrorMessage(new TranslationTextComponent("commands.superpower.error.doesntHaveSuperpower", entity.getDisplayName()));
                }
            } else {
                commandSource.sendErrorMessage(new TranslationTextComponent("commands.superpower.error.noLivingEntity"));
            }
        }

        if (!no) {
            if (i == 1) {
                commandSource.sendFeedback(new TranslationTextComponent("commands.superpower.remove.success.entity.single", (entities.iterator().next()).getDisplayName()), true);
            } else {
                commandSource.sendFeedback(new TranslationTextComponent("commands.superpower.remove.success.entity.multiple", i), true);
            }
        }

        return i;
    }

    public static int removeAllSuperpowers(CommandSource commandSource, Collection<? extends Entity> entities) {
        Iterator<? extends Entity> iterator = entities.iterator();
        int i = 0;

        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (entity instanceof LivingEntity) {
                SuperpowerManager.removeSuperpowers((LivingEntity) entity);
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