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
import net.threetag.palladium.power.SuperpowerUtil;
import net.threetag.palladium.util.property.PalladiumProperties;
import net.threetag.palladiumcore.util.Platform;

import java.util.*;
import java.util.function.Predicate;

public class SuperpowerCommand {

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_POWERS = (context, builder) -> {
        Collection<Power> powers = PowerManager.getInstance(Objects.requireNonNull(Platform.getCurrentServer()).overworld()).getPowers();
        return SharedSuggestionProvider.suggestResource(powers.stream().map(Power::getId), builder);
    };

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_OWN_POWERS = (context, builder) -> {
        List<String> superpowers = Lists.newArrayList();
        Collection<? extends Entity> entities;
        try {
            context.getArgument("entities", EntitySelector.class);
            entities = EntityArgument.getEntities(context, "entities");
        } catch (Exception e) {
            entities = Collections.singleton(context.getSource().getPlayerOrException());
        }
        for (Entity entity : entities) {
            for (ResourceLocation id : PalladiumProperties.SUPERPOWER_IDS.get(entity)) {
                var allId = id.getNamespace() + ":all";
                if (!superpowers.contains(allId)) {
                    superpowers.add(allId);
                }

                if (!superpowers.contains(id.toString())) {
                    superpowers.add(id.toString());
                }
            }
        }

        return SharedSuggestionProvider.suggest(superpowers, builder);
    };

    public static final DynamicCommandExceptionType POWER_NOT_FOUND = new DynamicCommandExceptionType((object) -> {
        return Component.translatable("commands.superpower.error.powerNotFound", object);
    });

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("superpower").requires((player) -> {
                    return player.hasPermission(2);
                })

                .then(Commands.literal("query")
                        .then(Commands.argument("entity", EntityArgument.entity()).executes(c -> {
                            return querySuperpowers(c.getSource(), EntityArgument.getEntity(c, "entity"));
                        })))

                .then(Commands.literal("set")
                        .then(Commands.argument("power", ResourceLocationArgument.id()).suggests(SUGGEST_POWERS).executes(c -> {
                                    return setSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), getSuperpower(c, "power"));
                                })
                                .then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                                    return setSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), getSuperpower(c, "power"));
                                }))))

                .then(Commands.literal("add")
                        .then(Commands.argument("power", ResourceLocationArgument.id()).suggests(SUGGEST_POWERS).executes(c -> {
                                    return addSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), getSuperpower(c, "power"));
                                })
                                .then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                                    return addSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), getSuperpower(c, "power"));
                                }))))

                .then(Commands.literal("remove")
                        .then(Commands.argument("power", ResourceLocationArgument.id()).suggests(SUGGEST_OWN_POWERS).executes(c -> {
                                    return removeSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), ResourceLocationArgument.getId(c, "power").toString());
                                })
                                .then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                                    return removeSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), ResourceLocationArgument.getId(c, "power").toString());
                                })))
                        .then(Commands.literal("*").executes(c -> {
                                    return removeSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), "all");
                                })
                                .then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                                    return removeSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), "all");
                                })))
                        .then(Commands.literal("all").executes(c -> {
                                    return removeSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), "all");
                                })
                                .then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                                    return removeSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), "all");
                                }))))

                .then(Commands.literal("replace")
                        .then(Commands.argument("replaced_power", ResourceLocationArgument.id()).suggests(SUGGEST_OWN_POWERS)
                                .then(Commands.argument("replacing_power", ResourceLocationArgument.id()).suggests(SUGGEST_POWERS).executes(c -> {
                                            return replaceSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), ResourceLocationArgument.getId(c, "replaced_power").toString(), ResourceLocationArgument.getId(c, "replacing_power"));
                                        })
                                        .then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                                            return replaceSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), ResourceLocationArgument.getId(c, "replaced_power").toString(), ResourceLocationArgument.getId(c, "replacing_power"));
                                        }))))
                        .then(Commands.literal("*")
                                .then(Commands.argument("replacing_power", ResourceLocationArgument.id()).suggests(SUGGEST_POWERS).executes(c -> {
                                            return replaceSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), "all", ResourceLocationArgument.getId(c, "replacing_power"));
                                        })
                                        .then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                                            return replaceSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), "all", ResourceLocationArgument.getId(c, "replacing_power"));
                                        }))))
                        .then(Commands.literal("all")
                                .then(Commands.argument("replacing_power", ResourceLocationArgument.id()).suggests(SUGGEST_POWERS).executes(c -> {
                                            return replaceSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), "all", ResourceLocationArgument.getId(c, "replacing_power"));
                                        })
                                        .then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                                            return replaceSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), "all", ResourceLocationArgument.getId(c, "replacing_power"));
                                        }))))));
    }

    private static int querySuperpowers(CommandSourceStack source, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            StringBuilder result = new StringBuilder();
            int i = 0;
            for (ResourceLocation id : SuperpowerUtil.getSuperpowerIds(livingEntity)) {
                result.append(id.toString()).append(", ");
                i++;
            }

            if(i == 0) {
                source.sendFailure(Component.translatable("commands.superpower.error.noSuperpowers", livingEntity.getDisplayName()));
            } else {
                source.sendSuccess(Component.translatable("commands.superpower.query.success", livingEntity.getDisplayName(), result.substring(0, result.length() - 3)), true);
            }

            return i;
        } else {
            source.sendFailure(Component.translatable("commands.superpower.error.noLivingEntity"));
            return 0;
        }
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
            if (entity instanceof LivingEntity livingEntity) {
                SuperpowerUtil.setSuperpower(livingEntity, power);
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
            if (entity instanceof LivingEntity livingEntity) {
                if (SuperpowerUtil.addSuperpower(livingEntity, superpower)) {
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

    public static int removeSuperpower(CommandSourceStack commandSource, Collection<? extends Entity> entities, String filter) {
        Iterator<? extends Entity> iterator = entities.iterator();
        int i = 0;
        boolean no = false;
        Predicate<ResourceLocation> predicate = filter.equalsIgnoreCase("all") ? id -> true : (filter.endsWith(":all") ? id -> id.getNamespace().equals(filter.split(":")[0]) : id -> id.equals(new ResourceLocation(filter)));

        while (iterator.hasNext()) {
            Entity entity = iterator.next();

            if (entity instanceof LivingEntity livingEntity) {
                if (SuperpowerUtil.removeSuperpowersByIds(livingEntity, predicate) > 0) {
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

    public static int replaceSuperpower(CommandSourceStack commandSource, Collection<? extends Entity> entities, String replacedFilter, ResourceLocation replacingId) {
        Iterator<? extends Entity> iterator = entities.iterator();
        int i = 0;
        boolean no = false;
        Predicate<ResourceLocation> predicate = replacedFilter.equalsIgnoreCase("all") ? id -> true : (replacedFilter.endsWith(":all") ? id -> id.getNamespace().equals(replacedFilter.split(":")[0]) : id -> id.equals(new ResourceLocation(replacedFilter)));

        while (iterator.hasNext()) {
            Entity entity = iterator.next();

            if (entity instanceof LivingEntity livingEntity) {
                if (SuperpowerUtil.removeSuperpowersByIds(livingEntity, predicate) > 0 && SuperpowerUtil.addSuperpower(livingEntity, replacingId)) {
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
                commandSource.sendSuccess(Component.translatable("commands.superpower.replace.success.entity.single", (entities.iterator().next()).getDisplayName()), true);
            } else {
                commandSource.sendSuccess(Component.translatable("commands.superpower.replace.success.entity.multiple", i), true);
            }
        }

        return i;
    }

}