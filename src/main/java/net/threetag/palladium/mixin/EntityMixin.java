package net.threetag.palladium.mixin;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.entity.PalladiumEntityExtension;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataType;
import net.threetag.palladium.entity.flight.EntityFlightHandler;
import net.threetag.palladium.entity.flight.FlightController;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Mixin(Entity.class)
public abstract class EntityMixin implements PalladiumEntityExtension {

    @Shadow
    public abstract RegistryAccess registryAccess();

    @Shadow
    private Level level;
    @Unique
    private Map<PalladiumEntityDataType<?>, PalladiumEntityData<?, ?>> palladium$dataMap;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(EntityType<?> entityType, Level level, CallbackInfo ci) {
        var entity = (Entity) (Object) this;
        this.palladium$dataMap = new HashMap<>();

        for (PalladiumEntityDataType<?> dataType : PalladiumRegistries.ENTITY_DATA_TYPE) {
            if (dataType.getPredicate().test(entity)) {
                var data = dataType.codec().codec().parse(level.registryAccess().createSerializationContext(NbtOps.INSTANCE), new CompoundTag()).getOrThrow();
                data.setEntity(entity);
                this.palladium$dataMap.put(dataType, data);
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Inject(method = "saveWithoutId", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;addAdditionalSaveData(Lnet/minecraft/world/level/storage/ValueOutput;)V", shift = At.Shift.AFTER))
    public void saveWithoutId(ValueOutput output, CallbackInfo ci) {
        ValueOutput palladiumTag = output.child(Palladium.MOD_ID);
        for (Map.Entry<PalladiumEntityDataType<?>, PalladiumEntityData<?, ?>> e : this.palladium$dataMap.entrySet()) {
            var id = PalladiumRegistries.ENTITY_DATA_TYPE.getKey(e.getKey());

            if (id != null) {
                Codec codec = e.getValue().codec().codec();
                palladiumTag.store(id.toString(), codec, e.getValue());
            }
        }
    }

    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/world/level/storage/ValueInput;)V", shift = At.Shift.AFTER))
    public void load(ValueInput input, CallbackInfo ci) {
        ValueInput palladiumTag = input.childOrEmpty(Palladium.MOD_ID);
        var entity = (Entity) (Object) this;

        for (PalladiumEntityDataType<?> dataType : PalladiumRegistries.ENTITY_DATA_TYPE) {
            if (dataType.getPredicate().test(entity)) {
                var id = PalladiumRegistries.ENTITY_DATA_TYPE.getKey(dataType);
                palladiumTag.read(Objects.requireNonNull(id).toString(), dataType.codec().codec()).ifPresent(data -> {
                    data.setEntity(entity);
                    this.palladium$dataMap.put(dataType, data);
                });
            }
        }
    }

    @Override
    public Map<PalladiumEntityDataType<?>, PalladiumEntityData<?, ?>> palladium$getDataMap() {
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

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Inject(method = "turn", at = @At("RETURN"))
    protected void turn(double yRot, double xRot, CallbackInfo ci) {
        if ((Object) this instanceof LivingEntity living) {
            var flight = EntityFlightHandler.get(living);
            FlightController flightController = flight.getController();

            if (flightController != null) {
                flightController.clampRotation(living, flight.getFlightType());
            }
        }
    }
}
