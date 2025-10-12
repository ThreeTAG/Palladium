package net.threetag.palladium.fabric.mixin;

import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import net.threetag.palladium.addonpack.AddonPackManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(PackRepository.class)
public class PackRepositoryMixin {

    @Mutable
    @Shadow
    @Final
    private Set<RepositorySource> sources;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(RepositorySource[] repositorySources, CallbackInfo ci) {
        for (RepositorySource source : repositorySources) {
            if (source instanceof BuiltInPackSource builtInPackSource) {
                this.sources = new HashSet<>(sources);
                this.sources.add(AddonPackManager.getWrappedPackFinder(builtInPackSource.packType));
                return;
            }
        }
    }

}
