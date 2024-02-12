package net.threetag.palladium.addonpack.builder;

import com.google.gson.JsonParseException;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AddonBuilder<T, B extends AddonBuilder<T, B>> implements Supplier<T> {

    private final ResourceLocation id;
    private T built;
    private ResourceLocation parentId;
    private B parent;

    protected AddonBuilder(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    protected abstract T create();

    @Override
    public T get() {
        if (this.built == null) {
            try {
                this.built = this.create();
            } catch (Exception e) {
                CrashReport report = CrashReport.forThrowable(e, "Error while building " + this.id);
                this.fillReport(report);
                throw new ReportedException(report);
            }
        }
        return this.built;
    }

    @Nullable
    public B getParent() {
        return this.parent;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <V> V getValue(Function<B, V> valueGetter) {
        var thisValue = valueGetter.apply((B) this);

        if (thisValue == null && this.getParent() != null) {
            return this.getParent().getValue(valueGetter);
        }

        return thisValue;
    }

    @NotNull
    public <V> V getValue(Function<B, V> valueGetter, @NotNull V fallback) {
        return Utils.orElse(this.getValue(valueGetter), fallback);
    }

    public void resolveParent(Map<ResourceLocation, B> entries) {
        if (this.parentId != null) {
            if (this.parentId.equals(this.id)) {
                throw new JsonParseException(this.id + " has itself set as a parent, not possible!");
            }

            this.parent = entries.get(this.parentId);

            if (this.parent == null) {
                throw new JsonParseException("Unknown parent '" + this.parentId + "' for " + this.id);
            }
        }
    }

    protected CrashReportCategory fillReport(CrashReport crashReport) {
        CrashReportCategory reportCategory = crashReport.addCategory("Addon", 1);
        reportCategory.setDetail("Resource name", this.id);
        return reportCategory;
    }
}
