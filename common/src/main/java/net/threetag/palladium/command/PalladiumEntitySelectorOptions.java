package net.threetag.palladium.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.mixin.EntitySelectorOptionsInvoker;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladiumcore.util.Platform;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class PalladiumEntitySelectorOptions {

    public static final Component POWER_DESCRIPTION = Component.translatable("argument.entity.options.palladium.power.description");

    public static void init() {
        EntitySelectorOptionsInvoker.callRegister("palladium.power",
                parser -> {
                    cast(parser).palladium$setPower(parsePowerId(parser));
                },
                parser -> cast(parser).palladium$getPower() == null,
                POWER_DESCRIPTION);
    }

    private static EntitySelectorParserExtension cast(EntitySelectorParser parser) {
        return (EntitySelectorParserExtension) parser;
    }

    private static ResourceLocation parsePowerId(EntitySelectorParser parser) throws CommandSyntaxException {
        var powers = PowerManager.getInstance(Platform.isServer());

        parser.setSuggestions((builder, consumer) -> {
            suggestIdentifiersIgnoringNamespace(Palladium.MOD_ID, powers.getIds(), builder);
            return builder.buildFuture();
        });
        return ResourceLocation.read(parser.getReader());
    }

    public static CompletableFuture<Suggestions> suggestIdentifiersIgnoringNamespace(String namespace, Iterable<ResourceLocation> candidates, SuggestionsBuilder builder) {
        forEachMatchingIgnoringNamespace(
                namespace,
                candidates,
                builder.getRemaining().toLowerCase(Locale.ROOT),
                Function.identity(),
                id -> builder.suggest(String.valueOf(id))
        );

        return builder.buildFuture();
    }

    public static <T> void forEachMatchingIgnoringNamespace(String namespace, Iterable<T> candidates, String string, Function<T, ResourceLocation> idFunc, Consumer<T> action) {
        final boolean hasColon = string.indexOf(':') > -1;

        ResourceLocation id;
        for (final T object : candidates) {
            id = idFunc.apply(object);
            if (hasColon) {
                if (wordStartsWith(string, id.toString(), '_')) {
                    action.accept(object);
                }
            } else if (
                    wordStartsWith(string, id.getNamespace(), '_') ||
                            id.getNamespace().equals(namespace) &&
                                    wordStartsWith(string, id.getPath(), '_')
            ) {
                action.accept(object);
            }
        }
    }

    public static boolean wordStartsWith(String string, String substring, char wordSeparator) {
        for (int i = 0; !substring.startsWith(string, i); i++) {
            i = substring.indexOf(wordSeparator, i);
            if (i < 0) {
                return false;
            }
        }

        return true;
    }

}
