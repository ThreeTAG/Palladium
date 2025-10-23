package net.threetag.palladium.command;

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.BlockTypes;
import net.neoforged.fml.loading.FMLPaths;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.item.ItemTypes;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class RegistryDumpCommand {

    private static final Map<ResourceKey<? extends Registry<?>>, RegistryDump<?>> REGISTRIES = new HashMap<>();

    static {
        addRegistry(Registries.ITEM, ItemTypes.CODEC);
        addRegistry(Registries.BLOCK, BlockTypes.CODEC.codec());
        addRegistry(PalladiumRegistryKeys.POWER, Power.CODEC);
    }

    public static <T> void addRegistry(ResourceKey<? extends Registry<T>> key, Codec<T> codec) {
        REGISTRIES.put(key, new RegistryDump<>(key, codec));
    }

    public static void register(LiteralArgumentBuilder<CommandSourceStack> builder, CommandBuildContext context) {
        builder.then(Commands.literal("registry-dump").requires((player) -> {
            return player.hasPermission(3);
        }).executes((c) -> {
            return dumpRegistry(c.getSource());
        }));
    }

    private static int dumpRegistry(CommandSourceStack source) {
        for (RegistryDump<?> dump : REGISTRIES.values()) {
            try {
                dump.dump(source.getServer());
            } catch (Exception e) {
                Palladium.LOGGER.error("Error while dumping registry {}", dump.registryKey.toString(), e);
                source.sendFailure(Component.literal("Error while dumping registry " + dump.registryKey));
            }
        }
        source.sendSuccess(() -> Component.literal("Successfully dumped registries"), true);
        return 1;
    }

    private record RegistryDump<T>(ResourceKey<? extends Registry<T>> registryKey, Codec<T> codec) {

        public void dump(MinecraftServer server) {
            Registry<T> registry = server.registryAccess().lookupOrThrow(this.registryKey);

            registry.entrySet().forEach((entry) -> {
                ResourceKey<T> key = entry.getKey();
                Path outputFile = FMLPaths.GAMEDIR.get()
                        .resolve("palladium")
                        .resolve("registry_dump")
                        .resolve(key.registry().getNamespace())
                        .resolve(key.registry().getPath())
                        .resolve(key.location().getNamespace())
                        .resolve(key.location().getPath() + ".json");

                try {
                    JsonElement json = this.codec.encodeStart(server.registryAccess().createSerializationContext(JsonOps.INSTANCE), entry.getValue()).getOrThrow();
                    ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                    HashingOutputStream hashingoutputstream = new HashingOutputStream(Hashing.sha1(), bytearrayoutputstream);
                    JsonWriter jsonwriter = new JsonWriter(new OutputStreamWriter(hashingoutputstream, StandardCharsets.UTF_8));

                    try {
                        jsonwriter.setSerializeNulls(false);
                        jsonwriter.setIndent(" ".repeat(Math.max(0, 2)));
                        GsonHelper.writeValue(jsonwriter, json, DataProvider.KEY_COMPARATOR);
                    } catch (Throwable var9) {
                        try {
                            jsonwriter.close();
                        } catch (Throwable var8) {
                            var9.addSuppressed(var8);
                        }
                        throw var9;
                    }

                    jsonwriter.close();
                    Files.createDirectories(outputFile.getParent());
                    Files.write(outputFile, bytearrayoutputstream.toByteArray());
                } catch (Exception e) {
                    Palladium.LOGGER.error("Error while {}", key.toString(), e);
                }
            });
        }

    }

}
