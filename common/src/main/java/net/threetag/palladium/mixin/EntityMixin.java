package net.threetag.palladium.mixin;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.threetag.palladium.entity.PalladiumEntityExtension;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataType;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.IntangibilityAbility;
import net.threetag.palladium.registry.PalladiumRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(Entity.class)
public abstract class EntityMixin implements PalladiumEntityExtension {

    @Shadow
    public abstract RegistryAccess registryAccess();

    @Shadow
    private Level level;
    @Unique
    private Map<PalladiumEntityDataType<?>, PalladiumEntityData<?>> palladium$dataMap;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(EntityType<?> entityType, Level level, CallbackInfo ci) {
        var entity = (Entity) (Object) this;
        this.palladium$dataMap = new HashMap<>();

        for (PalladiumEntityDataType<?> dataType : PalladiumRegistries.ENTITY_DATA_TYPE) {
            var data = dataType.make(entity);

            if (data != null) {
                this.palladium$dataMap.put(dataType, data);
            }
        }
    }

    @Inject(method = "saveWithoutId", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", shift = At.Shift.AFTER))
    public void saveWithoutId(CompoundTag compound, CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag palladiumTag = compound.contains("palladium") ? compound.getCompound("palladium") : new CompoundTag();
        for (Map.Entry<PalladiumEntityDataType<?>, PalladiumEntityData<?>> e : this.palladium$dataMap.entrySet()) {
            var id = PalladiumRegistries.ENTITY_DATA_TYPE.getKey(e.getKey());

            if (id != null) {
                palladiumTag.put(id.toString(), e.getValue().save(this.registryAccess()));
            }
        }
        compound.put("palladium", palladiumTag);
    }

    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", shift = At.Shift.AFTER))
    public void load(CompoundTag compound, CallbackInfo ci) {
        CompoundTag palladiumTag = compound.contains("palladium") ? compound.getCompound("palladium") : new CompoundTag();
        for (Map.Entry<PalladiumEntityDataType<?>, PalladiumEntityData<?>> e : this.palladium$dataMap.entrySet()) {
            var id = PalladiumRegistries.ENTITY_DATA_TYPE.getKey(e.getKey());

            if (id != null) {
                e.getValue().load(palladiumTag.getCompound(id.toString()), this.registryAccess());
            }
        }
    }

    @Override
    public Map<PalladiumEntityDataType<?>, PalladiumEntityData<?>> palladium$getDataMap() {
        return ImmutableMap.copyOf(this.palladium$dataMap);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;containing(DDD)Lnet/minecraft/core/BlockPos;"), method = "moveTowardsClosestSpace", cancellable = true)
    protected void pushOutOfBlocks(double x, double y, double z, CallbackInfo ci) {
        if ((Object) this instanceof LivingEntity living) {
            for (AbilityInstance<IntangibilityAbility> instance : AbilityUtil.getEnabledInstances(living, AbilitySerializers.INTANGIBILITY.get())) {
                if (IntangibilityAbility.canGoThrough(instance, this.level.getBlockState(BlockPos.containing(x, y, z)))) {
                    ci.cancel();
                    return;
                }
            }
        }
    }
}
