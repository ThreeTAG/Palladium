package net.threetag.palladium.addonpack.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.addonpack.builder.AddonBuilder;
import net.threetag.palladium.addonpack.log.AddonPackLog;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AddonParser<B extends AddonBuilder<?, B>> extends SimpleJsonResourceReloadListener {

    public static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public final ResourceKey<? extends Registry<?>> resourceKey;

    public AddonParser(Gson gson, String string, ResourceKey<? extends Registry<?>> resourceKey) {
        super(gson, string);
        this.resourceKey = resourceKey;
    }

    @SuppressWarnings({"unchecked", "rawtypes", "UnnecessaryLocalVariable"})
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.injectJsons(object);
        AtomicInteger i = new AtomicInteger();
        Map<ResourceLocation, B> addonBuilders = new HashMap<>();
        Map<String, List<String>> loadingOrders = new HashMap<>();

        object.forEach((id, jsonElement) -> {
            try {
                if (id.getPath().equalsIgnoreCase("_loading_order")) {
                    loadingOrders.put(id.getNamespace(), parseLoadingOrder(jsonElement));
                } else {
                    addonBuilders.put(id, parse(id, jsonElement));
                    i.getAndIncrement();
                }
            } catch (Exception e) {
                CrashReport crashReport = CrashReport.forThrowable(e, "Error while parsing addonpack " + this.resourceKey.location().getPath() + " '" + id + "'");

                CrashReportCategory reportCategory = crashReport.addCategory("Addon " + this.resourceKey.location().getPath(), 1);
                reportCategory.setDetail("Resource name", id);

                throw new ReportedException(crashReport);
            }
        });

        Map<ResourceLocation, B> sorted = new TreeMap<>((id1, id2) -> {
            if (id1.getNamespace().equals(id2.getNamespace())) {
                List<String> order = loadingOrders.get(id1.getNamespace());

                if (order == null) {
                    return id1.compareTo(id2);
                }

                if (order.contains(id1.getPath()) && !order.contains(id2.getPath())) {
                    return -1;
                } else if (!order.contains(id1.getPath()) && order.contains(id2.getPath())) {
                    return 1;
                } else if (!order.contains(id1.getPath()) && !order.contains(id2.getPath())) {
                    return id1.compareTo(id2);
                } else {
                    return order.indexOf(id1.getPath()) - order.indexOf(id2.getPath());
                }
            } else {
                return id1.compareTo(id2);
            }
        });
        sorted.putAll(addonBuilders);

        for (B addonBuilder : sorted.values()) {
            addonBuilder.resolveParent(sorted);
        }

        for (B addonBuilder : sorted.values()) {
            ResourceKey key1 = this.resourceKey;
            AddonBuilder builder1 = addonBuilder;
            register(key1, builder1);
            this.postRegister(addonBuilder);
        }

        AddonPackLog.info("Registered " + i.get() + " addonpack " + this.resourceKey.location().getPath());
    }

    public List<String> parseLoadingOrder(JsonElement jsonElement) {
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<String> list = new LinkedList<>();
        for (JsonElement element : jsonArray) {
            list.add(element.getAsString());
        }
        return list;
    }

    @ExpectPlatform
    public static <T> void register(ResourceKey<? extends Registry<T>> key, AddonBuilder<T, ?> builder) {
        throw new AssertionError();
    }

    public void postRegister(B addonBuilder) {

    }

    public void injectJsons(Map<ResourceLocation, JsonElement> map) {

    }

    public abstract B parse(ResourceLocation id, JsonElement jsonElement);
}
