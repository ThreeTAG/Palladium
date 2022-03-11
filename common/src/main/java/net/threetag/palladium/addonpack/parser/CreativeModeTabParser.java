package net.threetag.palladium.addonpack.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CreativeModeTabParser extends SimpleJsonResourceReloadListener {

    public CreativeModeTabParser() {
        super(AddonParser.GSON, "creative_mode_tabs");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        AtomicInteger i = new AtomicInteger();
        object.forEach((id, jsonElement) -> {
            try {
                JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "$");
                ResourceLocation icon = GsonUtil.getAsResourceLocation(json, "icon");
                CreativeTabRegistry.create(id, () -> new ItemStack(Registry.ITEM.get(icon)));
                i.getAndIncrement();
            } catch (Exception e) {
                CrashReport crashReport = CrashReport.forThrowable(e, "Error while parsing addonpack creative mode tab " + " '" + id + "'");

                CrashReportCategory reportCategory = crashReport.addCategory("Addon Creative Mode Tab", 1);
                reportCategory.setDetail("Resource name", id);

                throw new ReportedException(crashReport);
            }
        });

        Palladium.LOGGER.info("Registered " + i.get() + " addonpack creative mode tabs");
    }
}
