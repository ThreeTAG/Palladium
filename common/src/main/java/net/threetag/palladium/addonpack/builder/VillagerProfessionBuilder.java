package net.threetag.palladium.addonpack.builder;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Predicate;

public class VillagerProfessionBuilder extends AddonBuilder<VillagerProfession, VillagerProfessionBuilder> {

    private ResourceKey<PoiType> poiTypeKey;
    private ResourceLocation soundEventId;

    public VillagerProfessionBuilder(ResourceLocation id) {
        super(id);
    }

    public VillagerProfessionBuilder poiType(ResourceLocation poiTypeId) {
        this.poiTypeKey = ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, poiTypeId);
        return this;
    }

    public VillagerProfessionBuilder sound(ResourceLocation soundId) {
        this.soundEventId = soundId;
        return this;
    }

    @Override
    protected VillagerProfession create() {
        var sound = BuiltInRegistries.SOUND_EVENT.get((ResourceLocation) this.getValue(b -> b.soundEventId));
        Predicate<Holder<PoiType>> predicate = holder -> holder.is((ResourceKey<PoiType>) this.getValue(b -> b.poiTypeKey));

        return new VillagerProfession(this.getId().getPath(),
                predicate,
                predicate,
                ImmutableSet.of(), ImmutableSet.of(),
                sound
        );
    }

}
