package net.threetag.palladium.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
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
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerHandler;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.energybar.EnergyBarConfiguration;
import net.threetag.palladium.power.energybar.EnergyBarReference;

import java.util.List;

public class EnergyBarCommand {

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_OWN_POWERS = (context, builder) -> {
        List<ResourceLocation> powers = Lists.newArrayList();
        Entity entity;
        try {
            context.getArgument("entity", EntitySelector.class);
            entity = EntityArgument.getEntity(context, "entity");
        } catch (Exception e) {
            entity = context.getSource().getPlayerOrException();
        }
        if (entity instanceof LivingEntity living) {
            var manager = PowerManager.getPowerHandler(living).orElse(new PowerHandler(living));

            for (IPowerHolder holder : manager.getPowerHolders().values()) {
                if(!holder.getEnergyBars().isEmpty()) {
                    powers.add(holder.getPower().getId());
                }
            }
        }

        return SharedSuggestionProvider.suggestResource(powers, builder);
    };

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_ENERGY_BAR = (context, builder) -> {
        List<String> energyBars = Lists.newArrayList();
        Power power = null;
        try {
            context.getArgument("power", ResourceLocation.class);
            power = SuperpowerCommand.getSuperpower(context, "power");
        } catch (Exception ignored) {
        }

        if (power != null) {
            for (EnergyBarConfiguration energyBar : power.getEnergyBars()) {
                energyBars.add(energyBar.getName());
            }
        }

        return SharedSuggestionProvider.suggest(energyBars, builder);
    };

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("energybar").requires((player) -> {
                    return player.hasPermission(2);
                })
                .then(Commands.literal("value")
                        .then(Commands.literal("get")
                                .then(Commands.argument("entity", EntityArgument.entity())
                                        .then(Commands.argument("power", ResourceLocationArgument.id()).suggests(SUGGEST_OWN_POWERS)
                                                .then(Commands.argument("energybar", StringArgumentType.word()).suggests(SUGGEST_ENERGY_BAR)
                                                        .executes(context -> {
                                                            return getEnergyBarValue(context.getSource(), EntityArgument.getEntity(context, "entity"), ResourceLocationArgument.getId(context, "power"), StringArgumentType.getString(context, "energybar"));
                                                        })))))
                        .then(Commands.literal("set")
                                .then(Commands.argument("entity", EntityArgument.entity())
                                        .then(Commands.argument("power", ResourceLocationArgument.id()).suggests(SUGGEST_OWN_POWERS)
                                                .then(Commands.argument("energybar", StringArgumentType.word()).suggests(SUGGEST_ENERGY_BAR)
                                                        .then(Commands.argument("value", IntegerArgumentType.integer(0))
                                                                .executes(context -> {
                                                                    return setEnergyBarValue(context.getSource(), EntityArgument.getEntity(context, "entity"), ResourceLocationArgument.getId(context, "power"), StringArgumentType.getString(context, "energybar"), IntegerArgumentType.getInteger(context, "value"));
                                                                })))))))
                .then(Commands.literal("max")
                        .then(Commands.literal("get")
                                .then(Commands.argument("entity", EntityArgument.entity())
                                        .then(Commands.argument("power", ResourceLocationArgument.id()).suggests(SUGGEST_OWN_POWERS)
                                                .then(Commands.argument("energybar", StringArgumentType.word()).suggests(SUGGEST_ENERGY_BAR)
                                                        .executes(context -> {
                                                            return getEnergyBarMaxValue(context.getSource(), EntityArgument.getEntity(context, "entity"), ResourceLocationArgument.getId(context, "power"), StringArgumentType.getString(context, "energybar"));
                                                        })))))
                        .then(Commands.literal("set")
                                .then(Commands.argument("entity", EntityArgument.entity())
                                        .then(Commands.argument("power", ResourceLocationArgument.id()).suggests(SUGGEST_OWN_POWERS)
                                                .then(Commands.argument("energybar", StringArgumentType.word()).suggests(SUGGEST_ENERGY_BAR)
                                                        .then(Commands.argument("value", IntegerArgumentType.integer(1))
                                                                .executes(context -> {
                                                                    return setEnergyBarMaxValue(context.getSource(), EntityArgument.getEntity(context, "entity"), ResourceLocationArgument.getId(context, "power"), StringArgumentType.getString(context, "energybar"), IntegerArgumentType.getInteger(context, "value"));
                                                                }))))))
                        .then(Commands.literal("reset")
                                .then(Commands.argument("entity", EntityArgument.entity())
                                        .then(Commands.argument("power", ResourceLocationArgument.id()).suggests(SUGGEST_OWN_POWERS)
                                                .then(Commands.argument("energybar", StringArgumentType.word()).suggests(SUGGEST_ENERGY_BAR)
                                                        .executes(context -> {
                                                            return resetEnergyBarMaxValue(context.getSource(), EntityArgument.getEntity(context, "entity"), ResourceLocationArgument.getId(context, "power"), StringArgumentType.getString(context, "energybar"));
                                                        })))))));
    }

    public static int getEnergyBarValue(CommandSourceStack source, Entity entity, ResourceLocation powerId, String energyBarName) {
        if (entity instanceof LivingEntity living) {
            var bar = new EnergyBarReference(powerId, energyBarName).getEntry(living);

            if (bar != null) {
                source.sendSuccess(() -> Component.translatable("commands.energybar.value.get.success", entity.getDisplayName(), powerId.toString(), energyBarName, bar.get()), true);
                return bar.get();
            } else {
                source.sendFailure(Component.translatable("commands.energybar.error.energyBarNotFound"));
                return 0;
            }
        } else {
            source.sendFailure(Component.translatable("commands.energybar.error.noLivingEntity"));
            return 0;
        }
    }

    public static int setEnergyBarValue(CommandSourceStack source, Entity entity, ResourceLocation powerId, String energyBarName, int value) {
        if (entity instanceof LivingEntity living) {
            var bar = new EnergyBarReference(powerId, energyBarName).getEntry(living);

            if (bar != null) {
                bar.set(value);
                source.sendSuccess(() -> Component.translatable("commands.energybar.value.set.success", entity.getDisplayName(), powerId.toString(), energyBarName, bar.get()), true);
                return 1;
            } else {
                source.sendFailure(Component.translatable("commands.energybar.error.energyBarNotFound"));
                return 0;
            }
        } else {
            source.sendFailure(Component.translatable("commands.energybar.error.noLivingEntity"));
            return 0;
        }
    }

    public static int getEnergyBarMaxValue(CommandSourceStack source, Entity entity, ResourceLocation powerId, String energyBarName) {
        if (entity instanceof LivingEntity living) {
            var bar = new EnergyBarReference(powerId, energyBarName).getEntry(living);

            if (bar != null) {
                source.sendSuccess(() -> Component.translatable("commands.energybar.maxValue.get.success", entity.getDisplayName(), powerId.toString(), energyBarName, bar.getMax()), true);
                return bar.getMax();
            } else {
                source.sendFailure(Component.translatable("commands.energybar.error.energyBarNotFound"));
                return 0;
            }
        } else {
            source.sendFailure(Component.translatable("commands.energybar.error.noLivingEntity"));
            return 0;
        }
    }

    public static int setEnergyBarMaxValue(CommandSourceStack source, Entity entity, ResourceLocation powerId, String energyBarName, int value) {
        if (entity instanceof LivingEntity living) {
            var bar = new EnergyBarReference(powerId, energyBarName).getEntry(living);

            if (bar != null) {
                bar.setOverriddenMaxValue(value);
                source.sendSuccess(() -> Component.translatable("commands.energybar.maxValue.set.success", entity.getDisplayName(), powerId.toString(), energyBarName, bar.getMax()), true);
                return 1;
            } else {
                source.sendFailure(Component.translatable("commands.energybar.error.energyBarNotFound"));
                return 0;
            }
        } else {
            source.sendFailure(Component.translatable("commands.energybar.error.noLivingEntity"));
            return 0;
        }
    }

    public static int resetEnergyBarMaxValue(CommandSourceStack source, Entity entity, ResourceLocation powerId, String energyBarName) {
        if (entity instanceof LivingEntity living) {
            var bar = new EnergyBarReference(powerId, energyBarName).getEntry(living);

            if (bar != null) {
                bar.setOverriddenMaxValue(-1);
                source.sendSuccess(() -> Component.translatable("commands.energybar.maxValue.reset.success", entity.getDisplayName(), powerId.toString(), energyBarName, bar.getMax()), true);
                return 1;
            } else {
                source.sendFailure(Component.translatable("commands.energybar.error.energyBarNotFound"));
                return 0;
            }
        } else {
            source.sendFailure(Component.translatable("commands.energybar.error.noLivingEntity"));
            return 0;
        }
    }

}
