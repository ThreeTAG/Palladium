package net.threetag.palladium.addonpack.builder;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;

public class VillagerProfessionBuilder extends AddonBuilder<VillagerProfession> {

    private ResourceKey<PoiType> poiTypeKey;
    private ResourceLocation soundEventId;

    public VillagerProfessionBuilder(ResourceLocation id) {
        super(id);
    }

    public VillagerProfessionBuilder poiType(ResourceLocation poiTypeId) {
        this.poiTypeKey = ResourceKey.create(Registry.POINT_OF_INTEREST_TYPE_REGISTRY, poiTypeId);
        return this;
    }

    public VillagerProfessionBuilder sound(ResourceLocation soundId) {
        this.soundEventId = soundId;
        return this;
    }

    @Override
    protected VillagerProfession create() {
        var sound = Registry.SOUND_EVENT.get(this.soundEventId);

        return new VillagerProfession(this.getId().getPath(),
                holder -> holder.is(this.poiTypeKey),
                holder -> holder.is(this.poiTypeKey),
                ImmutableSet.of(), ImmutableSet.of(),
                sound
        );
    }

}
