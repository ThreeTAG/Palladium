package net.threetag.palladium.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.SuperpowerUtil;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class SuperpowerCommand {

    public static final String ERROR_NO_LIVING_ENTITY = "commands.palladium.superpower.error.no_living_entity";
    public static final String QUERY_SUCCESS = "commands.palladium.superpower.query.success";
    public static final String QUERY_NO_POWERS = "commands.palladium.superpower.query.error.no_powers";
    public static final String SET_SUCCESS_SINGLE = "commands.palladium.superpower.set.success.single";
    public static final String SET_SUCCESS_MULTIPLE = "commands.palladium.superpower.set.success.multiple";
    public static final String ADD_ERROR_ALREADY_HAS = "commands.palladium.superpower.add.error.already_has_power";
    public static final String ADD_ERROR_NOT_POSSIBLE = "commands.palladium.superpower.add.error.not_possible";
    public static final String ADD_ERROR_NONE_ADDED = "commands.palladium.superpower.add.error.none_added";
    public static final String ADD_SUCCESS_SINGLE = "commands.palladium.superpower.add.success.single";
    public static final String ADD_SUCCESS_MULTIPLE = "commands.palladium.superpower.add.success.multiple";
    public static final String REMOVE_ERROR_NO_MATCH = "commands.palladium.superpower.remove.error.no_match";
    public static final String REMOVE_SUCCESS_SINGLE = "commands.palladium.superpower.remove.success.single";
    public static final String REMOVE_SUCCESS_MULTIPLE = "commands.palladium.superpower.remove.success.multiple";
    public static final String REPLACE_SUCCESS_SINGLE = "commands.palladium.superpower.replace.success.single";
    public static final String REPLACE_SUCCESS_MULTIPLE = "commands.palladium.superpower.replace.success.multiple";
    public static final String REPLACE_ERROR_NOT_POSSIBLE = "commands.palladium.superpower.replace.error.not_possible";
    public static final String REPLACE_ERROR_NONE_ADDED = "commands.palladium.superpower.replace.error.none_added";

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_OWN_POWERS_ALL = (context, builder) -> {
        List<ResourceLocation> superpowers = Lists.newArrayList();
        Collection<? extends Entity> entities;
        try {
            context.getArgument("entities", EntitySelector.class);
            entities = EntityArgument.getEntities(context, "entities");
        } catch (Exception e) {
            entities = Collections.singleton(context.getSource().getPlayerOrException());
        }
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) {
                SuperpowerUtil.getSuperpowers(livingEntity).stream().map(powerHolder -> powerHolder.unwrapKey().orElseThrow().location()).forEach(id -> {
                    var allId = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "all");
                    if (!superpowers.contains(allId)) {
                        superpowers.add(allId);
                    }

                    if (!superpowers.contains(id)) {
                        superpowers.add(id);
                    }
                });
            }
        }

        return SharedSuggestionProvider.suggestResource(superpowers, builder);
    };

    public static void register(LiteralArgumentBuilder<CommandSourceStack> builder, CommandBuildContext context) {
        builder.then(Commands.literal("superpower").requires((player) -> {
                    return player.hasPermission(2);
                })

                .then(Commands.literal("query")
                        .then(Commands.argument("entity", EntityArgument.entity()).executes(c -> {
                            return querySuperpowers(c.getSource(), EntityArgument.getEntity(c, "entity"));
                        })))

                .then(Commands.literal("set")
                        .then(Commands.argument("power", ResourceArgument.resource(context, PalladiumRegistryKeys.POWER)).executes(c -> {
                                    return setSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), ResourceArgument.getResource(c, "power", PalladiumRegistryKeys.POWER));
                                })
                                .then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                                    return setSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), ResourceArgument.getResource(c, "power", PalladiumRegistryKeys.POWER));
                                }))))

                .then(Commands.literal("add")
                        .then(Commands.argument("power", ResourceArgument.resource(context, PalladiumRegistryKeys.POWER)).executes(c -> {
                                    return addSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), ResourceArgument.getResource(c, "power", PalladiumRegistryKeys.POWER));
                                })
                                .then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                                    return addSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), ResourceArgument.getResource(c, "power", PalladiumRegistryKeys.POWER));
                                }))))

                .then(Commands.literal("remove")
                        .then(Commands.argument("power", ResourceArgument.resource(context, PalladiumRegistryKeys.POWER)).suggests(SUGGEST_OWN_POWERS_ALL).executes(c -> {
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
                        .then(Commands.argument("replaced_power", ResourceArgument.resource(context, PalladiumRegistryKeys.POWER)).suggests(SUGGEST_OWN_POWERS_ALL)
                                .then(Commands.argument("replacing_power", ResourceArgument.resource(context, PalladiumRegistryKeys.POWER)).executes(c -> {
                                            return replaceSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), ResourceLocationArgument.getId(c, "replaced_power").toString(), ResourceArgument.getResource(c, "replacing_power", PalladiumRegistryKeys.POWER));
                                        })
                                        .then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                                            return replaceSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), ResourceLocationArgument.getId(c, "replaced_power").toString(), ResourceArgument.getResource(c, "replacing_power", PalladiumRegistryKeys.POWER));
                                        }))))
                        .then(Commands.literal("*")
                                .then(Commands.argument("replacing_power", ResourceArgument.resource(context, PalladiumRegistryKeys.POWER)).executes(c -> {
                                            return replaceSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), "all", ResourceArgument.getResource(c, "replacing_power", PalladiumRegistryKeys.POWER));
                                        })
                                        .then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                                            return replaceSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), "all", ResourceArgument.getResource(c, "replacing_power", PalladiumRegistryKeys.POWER));
                                        }))))
                        .then(Commands.literal("all")
                                .then(Commands.argument("replacing_power", ResourceArgument.resource(context, PalladiumRegistryKeys.POWER)).executes(c -> {
                                            return replaceSuperpower(c.getSource(), Collections.singleton(c.getSource().getPlayerOrException()), "all", ResourceArgument.getResource(c, "replacing_power", PalladiumRegistryKeys.POWER));
                                        })
                                        .then(Commands.argument("entities", EntityArgument.entities()).executes(c -> {
                                            return replaceSuperpower(c.getSource(), EntityArgument.getEntities(c, "entities"), "all", ResourceArgument.getResource(c, "replacing_power", PalladiumRegistryKeys.POWER));
                                        }))))));
    }

    private static int querySuperpowers(CommandSourceStack source, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {

            MutableComponent powerList = null;
            AtomicInteger i = new AtomicInteger();

            for (Holder<Power> power : SuperpowerUtil.getSuperpowers(livingEntity)) {
                var powerName = power.value().getName().copy().setStyle(Style.EMPTY.withHoverEvent(new HoverEvent.ShowText(Component.literal(power.unwrapKey().orElseThrow().location().toString()))));

                if (powerList == null) {
                    powerList = powerName;
                } else {
                    powerList.append(", ").append(powerName);
                }

                i.getAndIncrement();
            }

            if (i.get() == 0) {
                source.sendFailure(Component.translatableEscape(QUERY_NO_POWERS, livingEntity.getDisplayName()));
            } else {
                MutableComponent finalPowerList = powerList;
                source.sendSuccess(() -> Component.translatableEscape(QUERY_SUCCESS, livingEntity.getDisplayName(), finalPowerList), true);
            }

            return i.get();
        } else {
            source.sendFailure(Component.translatableEscape(ERROR_NO_LIVING_ENTITY));
            return 0;
        }
    }

    public static int setSuperpower(CommandSourceStack commandSource, Collection<? extends Entity> entities, Holder<Power> power) {
        Iterator<? extends Entity> iterator = entities.iterator();
        int i = 0;

        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (entity instanceof LivingEntity livingEntity) {
                SuperpowerUtil.setSuperpower(livingEntity, power);
                i++;
            } else {
                commandSource.sendFailure(Component.translatableEscape(ERROR_NO_LIVING_ENTITY));
            }
        }

        if (i == 1) {
            commandSource.sendSuccess(() -> Component.translatableEscape(SET_SUCCESS_SINGLE, (entities.iterator().next()).getDisplayName(), power.value().getName()), true);
        } else {
            int finalI = i;
            commandSource.sendSuccess(() -> Component.translatableEscape(SET_SUCCESS_MULTIPLE, finalI, power.value().getName()), true);
        }

        return i;
    }

    public static int addSuperpower(CommandSourceStack commandSource, Collection<? extends Entity> entities, Holder<Power> superpower) {
        if (entities.size() == 1) {
            var entity = entities.stream().findFirst().get();

            if (entity instanceof LivingEntity living) {
                if (SuperpowerUtil.canSuperpowerBeAdded(living, superpower)) {
                    SuperpowerUtil.addSuperpower(living, superpower);
                    commandSource.sendSuccess(() -> Component.translatableEscape(ADD_SUCCESS_SINGLE, entity.getDisplayName(), superpower.value().getName()), true);
                    return 1;
                } else {
                    commandSource.sendFailure(Component.translatableEscape(ADD_ERROR_NOT_POSSIBLE, entity.getDisplayName()));
                }
            } else {
                commandSource.sendFailure(Component.translatableEscape(ERROR_NO_LIVING_ENTITY));
            }
        } else {
            int i = 0;

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity living) {
                    if (SuperpowerUtil.addSuperpower(living, superpower)) {
                        i++;
                    }
                }
            }

            if (i == 0) {
                commandSource.sendFailure(Component.translatableEscape(ADD_ERROR_NONE_ADDED, superpower.value().getName()));
            } else {
                commandSource.sendSuccess(() -> Component.translatableEscape(ADD_SUCCESS_MULTIPLE, entities.size(), superpower.value().getName()), true);
            }
        }

        return 0;
    }

    public static int removeSuperpower(CommandSourceStack commandSource, Collection<? extends Entity> entities, String filter) {
        int i = 0;
        Predicate<ResourceLocation> predicate = filter.equalsIgnoreCase("all") ? id -> true : (filter.endsWith(":all") ? id -> id.getNamespace().equals(filter.split(":")[0]) : id -> id.equals(ResourceLocation.parse(filter)));

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) {
                if (SuperpowerUtil.removeSuperpower(livingEntity, powerHolder -> predicate.test(powerHolder.unwrapKey().orElseThrow().location()))) {
                    i++;
                } else if (entities.size() == 1) {
                    commandSource.sendFailure(Component.translatable(REMOVE_ERROR_NO_MATCH, entity.getDisplayName()));
                    return 0;
                }
            }
        }

        if (i == 1) {
            commandSource.sendSuccess(() -> Component.translatableEscape(REMOVE_SUCCESS_SINGLE, (entities.iterator().next()).getDisplayName()), true);
        } else {
            int finalI = i;
            commandSource.sendSuccess(() -> Component.translatableEscape(REMOVE_SUCCESS_MULTIPLE, finalI), true);
        }

        return i;
    }

    public static int replaceSuperpower(CommandSourceStack commandSource, Collection<? extends Entity> entities, String replacedFilter, Holder<Power> replacingPower) {
        Predicate<ResourceLocation> predicate = replacedFilter.equalsIgnoreCase("all") ? id -> true : (replacedFilter.endsWith(":all") ? id -> id.getNamespace().equals(replacedFilter.split(":")[0]) : id -> id.equals(ResourceLocation.parse(replacedFilter)));

        if (entities.size() == 1) {
            var entity = entities.stream().findFirst().get();

            if (entity instanceof LivingEntity living) {
                SuperpowerUtil.removeSuperpower(living, powerHolder -> predicate.test(powerHolder.unwrapKey().orElseThrow().location()));

                if (SuperpowerUtil.canSuperpowerBeAdded(living, replacingPower)) {
                    SuperpowerUtil.addSuperpower(living, replacingPower);
                    commandSource.sendSuccess(() -> Component.translatableEscape(REPLACE_SUCCESS_SINGLE, entity.getDisplayName(), replacingPower.value().getName()), true);
                    return 1;
                } else {
                    commandSource.sendFailure(Component.translatableEscape(REPLACE_ERROR_NOT_POSSIBLE, entity.getDisplayName()));
                }
            } else {
                commandSource.sendFailure(Component.translatableEscape(ERROR_NO_LIVING_ENTITY));
            }
        } else {
            int i = 0;

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity living) {
                    SuperpowerUtil.removeSuperpower(living, powerHolder -> predicate.test(powerHolder.unwrapKey().orElseThrow().location()));

                    if (SuperpowerUtil.addSuperpower(living, replacingPower)) {
                        i++;
                    }
                }
            }

            if (i == 0) {
                commandSource.sendFailure(Component.translatableEscape(REPLACE_ERROR_NONE_ADDED, replacingPower.value().getName()));
            } else {
                commandSource.sendSuccess(() -> Component.translatableEscape(REPLACE_SUCCESS_MULTIPLE, entities.size(), replacingPower.value().getName()), true);
            }

            return i;
        }

        return 0;
    }

}