package net.threetag.palladium.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
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
import net.threetag.palladium.condition.BuyableCondition;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerHandler;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityEntry;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AbilityCommand {

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_OWN_POWERS = (context, builder) -> {
        List<ResourceLocation> powers = Lists.newArrayList();
        Collection<? extends Entity> entities;
        try {
            context.getArgument("entities", EntitySelector.class);
            entities = EntityArgument.getEntities(context, "entities");
        } catch (Exception e) {
            entities = Collections.singleton(context.getSource().getPlayerOrException());
        }
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity living) {
                var manager = PowerManager.getPowerHandler(living).orElse(new PowerHandler(living));

                for (IPowerHolder holder : manager.getPowerHolders().values()) {
                    for (AbilityEntry entry : holder.getAbilities().values()) {
                        if (entry.getConfiguration().isBuyable()) {
                            if (!powers.contains(holder.getPower().getId())) {
                                powers.add(holder.getPower().getId());
                            }
                            break;
                        }
                    }
                }
            }
        }

        return SharedSuggestionProvider.suggestResource(powers, builder);
    };

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_ABILITIES = (context, builder) -> {
        List<String> abilities = Lists.newArrayList();
        Power power = null;
        try {
            context.getArgument("power", ResourceLocation.class);
            power = SuperpowerCommand.getSuperpower(context, "power");
        } catch (Exception e) {
        }

        if (power != null) {
            for (AbilityConfiguration ability : power.getAbilities()) {
                if (ability.isBuyable()) {
                    abilities.add(ability.getId());
                }
            }
        }

        return SharedSuggestionProvider.suggest(abilities, builder);
    };

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("ability").requires((player) -> {
                    return player.hasPermission(2);
                })
                .then(Commands.literal("lock")
                        .then(Commands.argument("entities", EntityArgument.entities())
                                .then(Commands.argument("power", ResourceLocationArgument.id()).suggests(SUGGEST_OWN_POWERS)
                                        .then(Commands.argument("ability", StringArgumentType.word()).suggests(SUGGEST_ABILITIES)
                                                .executes(context -> {
                                                    return lockAbility(context.getSource(), EntityArgument.getEntities(context, "entities"), SuperpowerCommand.getSuperpower(context, "power"), StringArgumentType.getString(context, "ability"), true);
                                                })))))
                .then(Commands.literal("unlock")
                        .then(Commands.argument("entities", EntityArgument.entities())
                                .then(Commands.argument("power", ResourceLocationArgument.id()).suggests(SUGGEST_OWN_POWERS)
                                        .then(Commands.argument("ability", StringArgumentType.word()).suggests(SUGGEST_ABILITIES)
                                                .executes(context -> {
                                                    return lockAbility(context.getSource(), EntityArgument.getEntities(context, "entities"), SuperpowerCommand.getSuperpower(context, "power"), StringArgumentType.getString(context, "ability"), false);
                                                }))))));
    }

    public static int lockAbility(CommandSourceStack source, Collection<? extends Entity> entities, Power power, String abilityKey, boolean locking) {
        AbilityConfiguration configuration = power.getAbilities().stream().filter(c -> c.getId().equals(abilityKey)).findFirst().orElse(null);

        if (configuration == null || !configuration.isBuyable()) {
            source.sendFailure(Component.translatable("commands.ability.error.notUnlockable", abilityKey, power.getId()));
            return 0;
        }

        int i = 0;
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity living) {
                var holder = PowerManager.getPowerHandler(living).orElse(new PowerHandler(living)).getPowerHolder(power);

                if (holder != null) {
                    var ability = holder.getAbilities().get(abilityKey);

                    if (ability != null) {
                        ability.setUniqueProperty(BuyableCondition.BOUGHT, !locking);
                        i++;
                    }
                } else {
                    source.sendFailure(Component.translatable("commands.ability.error.doesntHavePower"));
                }
            } else {
                source.sendFailure(Component.translatable("commands.superpower.error.noLivingEntity"));
            }
        }

        int finalI = i;
        source.sendSuccess(() -> Component.translatable("commands.ability." + (locking ? "locking" : "unlocking") + ".success", abilityKey, power.getId(), finalI), true);

        return i;
    }

}
