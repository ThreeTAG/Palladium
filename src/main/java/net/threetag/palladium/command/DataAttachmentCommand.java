package net.threetag.palladium.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.serialization.DataResult;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.PermissionCheck;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.threetag.palladium.addonpack.DataAttachmentLoader;
import net.threetag.palladium.attachment.PackAttachmentBuilder;

import java.util.Collection;
import java.util.Optional;

public class DataAttachmentCommand {

    public static final PermissionCheck PERMISSION_CHECK = new PermissionCheck.Require(Permissions.COMMANDS_GAMEMASTER);
    private static final SuggestionProvider<CommandSourceStack> SUGGEST_DATA_TYPES = (context, builder) -> {
        return SharedSuggestionProvider.suggestResource(DataAttachmentLoader.INSTANCE.all().keySet(), builder);
    };

    public static final String TRANS_UNKNOWN_TYPE = "commands.palladium.data-attachment.error.unknown_type";
    public static final String TRANS_NO_DATA = "commands.palladium.data-attachment.entity.no_data";
    public static final String TRANS_GET_DATA = "commands.palladium.data-attachment.entity.get";
    public static final String TRANS_PARSE_ERROR = "commands.palladium.data-attachment.error.parse";
    public static final String TRANS_SET_DATA_SINGLE = "commands.palladium.data-attachment.entity.set.success.single";
    public static final String TRANS_SET_DATA_MULTIPLE = "commands.palladium.data-attachment.entity.set.success.multiple";

    private static final DynamicCommandExceptionType ERROR_UNKNOWN_TYPE = new DynamicCommandExceptionType((object) -> Component.translatableEscape(TRANS_UNKNOWN_TYPE, object));
    private static final SimpleCommandExceptionType ERROR_PARSE = new SimpleCommandExceptionType(Component.translatableEscape(TRANS_PARSE_ERROR));

    public static void register(LiteralArgumentBuilder<CommandSourceStack> builder, CommandBuildContext context) {
        builder.then(Commands.literal("data-attachment").requires(Commands.hasPermission(PERMISSION_CHECK))
                .then(Commands.literal("get")
                        .then(Commands.literal("entity")
                                .then(Commands.argument("entity", EntityArgument.entity())
                                        .then(Commands.argument("type", IdentifierArgument.id()).suggests(SUGGEST_DATA_TYPES)
                                                .executes(c -> {
                                                    return get(c.getSource(), EntityArgument.getEntity(c, "entity"), IdentifierArgument.getId(c, "type"));
                                                })))))
                .then(Commands.literal("set")
                        .then(Commands.literal("entity")
                                .then(Commands.argument("entities", EntityArgument.entities())
                                        .then(Commands.argument("type", IdentifierArgument.id()).suggests(SUGGEST_DATA_TYPES)
                                                .then(Commands.argument("value", StringArgumentType.string())
                                                        .executes(c -> {
                                                            return set(c.getSource(), EntityArgument.getEntities(c, "entities"), IdentifierArgument.getId(c, "type"), StringArgumentType.getString(c, "value"));
                                                        })))))));
    }

    public static int get(CommandSourceStack source, Entity entity, Identifier typeId) throws CommandSyntaxException {
        AttachmentType<?> type = NeoForgeRegistries.ATTACHMENT_TYPES.getValue(typeId);

        if (type == null) {
            throw ERROR_UNKNOWN_TYPE.create(typeId.toString());
        }

        Object value = entity.getData(type);
        source.sendSuccess(() -> Component.translatableEscape(TRANS_GET_DATA, typeId, entity.getDisplayName(), value), true);

        return switch (value) {
            case Number number -> number.intValue();
            case Boolean bool -> bool ? 1 : 0;
            case String string -> string.length();
            default -> 1;
        };
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static int set(CommandSourceStack source, Collection<? extends Entity> entities, Identifier typeId, String rawValue) throws CommandSyntaxException {
        PackAttachmentBuilder type = DataAttachmentLoader.INSTANCE.get(typeId);

        if (type == null) {
            throw ERROR_UNKNOWN_TYPE.create(typeId.toString());
        }

        Tag tag = TagParser.parseCompoundFully(rawValue);

        DataResult dataResult = type.codec().codec().parse(source.registryAccess().createSerializationContext(NbtOps.INSTANCE), tag);
        Optional<Object> optional = dataResult.result();

        if (optional.isEmpty()) {
            throw ERROR_PARSE.create();
        }

        Object value = optional.get();

        for (Entity entity : entities) {
            entity.setData(type.getType(), value);
        }

        if (entities.size() == 1) {
            source.sendSuccess(() -> Component.translatableEscape(TRANS_SET_DATA_SINGLE, typeId, entities.stream().findFirst().get().getDisplayName(), value), true);
        } else {
            source.sendSuccess(() -> Component.translatableEscape(TRANS_SET_DATA_MULTIPLE, typeId, entities.size(), value), true);
        }

        return entities.size();
    }

}
