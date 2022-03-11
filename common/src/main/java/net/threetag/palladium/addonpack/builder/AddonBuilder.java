package net.threetag.palladium.addonpack.builder;

import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public abstract class AddonBuilder<T> implements Supplier<T> {

    private final ResourceLocation id;
    private T built;

    protected AddonBuilder(ResourceLocation id) {
        this.id = id;
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

    protected CrashReportCategory fillReport(CrashReport crashReport) {
        CrashReportCategory reportCategory = crashReport.addCategory("Addon", 1);
        reportCategory.setDetail("Resource name", this.id);
        return reportCategory;
    }
}
