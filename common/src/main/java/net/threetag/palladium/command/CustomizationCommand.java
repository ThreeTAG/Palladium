package net.threetag.palladium.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.customization.EntityCustomizationHandler;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import org.jetbrains.annotations.Nullable;

public class CustomizationCommand {

    public static void register() {
        PalladiumCommand.COMMAND_CALLBACK.register((cmd, context) -> {
            cmd.then(Commands.literal("customization").requires((player) -> {
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
        });
    }

    private static int unlock(CommandSourceStack commandSource, Holder<Customization> customization, @Nullable Entity entity) throws CommandSyntaxException {
        if (entity == null) {
            entity = commandSource.getPlayerOrException();
        }

        // TODO
        if (entity instanceof LivingEntity living) {
            var handler = EntityCustomizationHandler.get(living);

            if (handler.unlock(customization)) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    private static int lock(CommandSourceStack commandSource, Holder<Customization> customization, @Nullable Entity entity) throws CommandSyntaxException {
        if (entity == null) {
            entity = commandSource.getPlayerOrException();
        }

        // TODO
        if (entity instanceof LivingEntity living) {
            var handler = EntityCustomizationHandler.get(living);

            if (handler.lock(customization)) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

}
