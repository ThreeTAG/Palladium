package net.threetag.palladium.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.customization.EntityCustomizationHandler;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import org.jetbrains.annotations.Nullable;

public class CustomizationCommand {

    public static final String UNLOCK_SUCCESS = "commands.palladium.customization.unlock.success";
    public static final String LOCK_SUCCESS = "commands.palladium.customization.lock.success";

    public static final String ERROR_NOT_UNLOCKABLE = "commands.palladium.customization.error.not_unlockable";
    public static final String ERROR_CANT_HAVE_CUSTOMIZATIONS = "commands.palladium.customization.error.cant_have_customizations";

    public static void register(LiteralArgumentBuilder<CommandSourceStack> builder, CommandBuildContext context) {
        builder.then(Commands.literal("customization").requires((player) -> {
                            return player.hasPermission(3);
                        })
                        .then(Commands.literal("unlock")
                                .then(Commands.argument("customization", ResourceArgument.resource(context, PalladiumRegistryKeys.CUSTOMIZATION))
                                        .executes(c ->
                                                unlock(
                                                        c.getSource(),
                                                        ResourceArgument.getResource(c, "customization", PalladiumRegistryKeys.CUSTOMIZATION),
                                                        null
                                                )
                                        )
                                        .then(Commands.argument("entity", EntityArgument.entity())
                                                .executes(c ->
                                                        unlock(
                                                                c.getSource(),
                                                                ResourceArgument.getResource(c, "customization", PalladiumRegistryKeys.CUSTOMIZATION),
                                                                EntityArgument.getEntity(c, "entity")
                                                        )
                                                ))
                                )
                        )
                        .then(Commands.literal("lock")
                                .then(Commands.argument("customization", ResourceArgument.resource(context, PalladiumRegistryKeys.CUSTOMIZATION))
                                        .executes(c ->
                                                lock(
                                                        c.getSource(),
                                                        ResourceArgument.getResource(c, "customization", PalladiumRegistryKeys.CUSTOMIZATION),
                                                        null
                                                )
                                        )
                                        .then(Commands.argument("entity", EntityArgument.entity())
                                                .executes(c ->
                                                        lock(
                                                                c.getSource(),
                                                                ResourceArgument.getResource(c, "customization", PalladiumRegistryKeys.CUSTOMIZATION),
                                                                EntityArgument.getEntity(c, "entity")
                                                        )
                                                ))
                                )
                        )
        );
    }

    private static int unlock(CommandSourceStack commandSource, Holder<Customization> customization, @Nullable Entity entity) throws CommandSyntaxException {
        if (entity == null) {
            entity = commandSource.getPlayerOrException();
        }

        if (entity instanceof LivingEntity living) {
            var handler = EntityCustomizationHandler.get(living);

            if (handler.unlock(customization)) {
                commandSource.sendSuccess(() -> Component.translatable(UNLOCK_SUCCESS, customization.value().getTitle(commandSource.registryAccess()), living.getDisplayName()), true);
                return 1;
            } else {
                commandSource.sendFailure(Component.translatable(ERROR_NOT_UNLOCKABLE, customization.value().getTitle(commandSource.registryAccess())));
                return 0;
            }
        } else {
            commandSource.sendFailure(Component.translatable(ERROR_CANT_HAVE_CUSTOMIZATIONS));
            return 0;
        }
    }

    private static int lock(CommandSourceStack commandSource, Holder<Customization> customization, @Nullable Entity entity) throws CommandSyntaxException {
        if (entity == null) {
            entity = commandSource.getPlayerOrException();
        }

        if (entity instanceof LivingEntity living) {
            var handler = EntityCustomizationHandler.get(living);

            if (handler.lock(customization)) {
                commandSource.sendSuccess(() -> Component.translatable(LOCK_SUCCESS, customization.value().getTitle(commandSource.registryAccess()), living.getDisplayName()), true);
                return 1;
            } else {
                commandSource.sendFailure(Component.translatable(ERROR_NOT_UNLOCKABLE, customization.value().getTitle(commandSource.registryAccess())));
                return 0;
            }
        } else {
            commandSource.sendFailure(Component.translatable(ERROR_CANT_HAVE_CUSTOMIZATIONS));
            return 0;
        }
    }

}
